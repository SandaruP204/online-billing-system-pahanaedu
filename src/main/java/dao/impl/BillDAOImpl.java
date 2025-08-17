package dao.impl;

import dao.BillDAO;                 // <-- interface
import dao.DBConnection;
import model.Bill;
import model.BillItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAOImpl implements BillDAO {

    private Connection testConn = null;

    public BillDAOImpl() {}
    public BillDAOImpl(Connection conn) { this.testConn = conn; }

    private Connection getConnection() throws Exception {
        if (testConn != null) return testConn;
        return DBConnection.getConnection();
    }

    private boolean shouldClose(Connection conn) {
        return testConn == null && conn != null;
    }

    @Override
    public int addBill(Bill bill) throws Exception {
        int generatedBillId = -1;

        // Normalize items list
        if (bill.getItems() == null) bill.setItems(new ArrayList<>());

        // Validate inputs early
        if (bill.getItems().isEmpty())
            throw new IllegalArgumentException("Bill must contain at least one item.");
        for (BillItem it : bill.getItems()) {
            if (it.getQuantity() <= 0)
                throw new IllegalArgumentException("Item quantity must be > 0 for product " + it.getProductNo());
        }

        // Compute total server-side using current prices
        double totalAmount = 0;
        for (BillItem item : bill.getItems()) {
            totalAmount += getProductPrice(item.getProductNo()) * item.getQuantity();
        }

        final String getCustomerSQL =
                "SELECT name FROM customers WHERE accountNo = ?";
        final String insertBillSQL =
                "INSERT INTO bills (accountNo, customer_name, bill_date, total_amount) VALUES (?, ?, ?, ?)";
        final String insertItemSQL =
                "INSERT INTO bill_items (bill_id, productNo, quantity) VALUES (?, ?, ?)";
        // Atomic stock decrement to prevent race conditions
        final String decStockSQL =
                "UPDATE products SET unit = unit - ? WHERE productNo = ? AND unit >= ?";

        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);

            // 1) Resolve customer name
            String customerName;
            try (PreparedStatement ps = conn.prepareStatement(getCustomerSQL)) {
                ps.setInt(1, bill.getAccountNo());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) customerName = rs.getString("name");
                    else throw new Exception("Customer does not exist (accountNo=" + bill.getAccountNo() + ")");
                }
            }

            // 2) Insert bill header
            try (PreparedStatement ps = conn.prepareStatement(insertBillSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, bill.getAccountNo());
                ps.setString(2, customerName);
                ps.setTimestamp(3, new Timestamp(bill.getBillDate().getTime()));
                ps.setDouble(4, totalAmount);
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) generatedBillId = keys.getInt(1);
                    else throw new SQLException("Failed to generate bill_id.");
                }
            }

            bill.setCustomerName(customerName);
            bill.setTotalAmount(totalAmount);

            // 3) Insert items + decrement stock (each row guarded)
            try (PreparedStatement psItem = conn.prepareStatement(insertItemSQL);
                 PreparedStatement psDec  = conn.prepareStatement(decStockSQL)) {

                for (BillItem item : bill.getItems()) {
                    // Stock decrement is atomic and prevents going below zero
                    psDec.setInt(1, item.getQuantity());
                    psDec.setInt(2, item.getProductNo());
                    psDec.setInt(3, item.getQuantity());
                    int updated = psDec.executeUpdate();
                    if (updated == 0) {
                        throw new Exception("Insufficient stock for product " + item.getProductNo());
                    }

                    // If decrement succeeded, record the item
                    psItem.setInt(1, generatedBillId);
                    psItem.setInt(2, item.getProductNo());
                    psItem.setInt(3, item.getQuantity());
                    psItem.addBatch();
                }

                psItem.executeBatch();
            }

            conn.commit();
            return generatedBillId;

        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (shouldClose(conn)) conn.close();
        }
    }

    @Override
    public double getProductPrice(int productNo) throws Exception {
        final String sql = "SELECT price FROM products WHERE productNo = ?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("price");
                throw new Exception("Product not found (productNo=" + productNo + ")");
            }
        } finally {
            if (shouldClose(conn)) conn.close();
        }
    }

    @Override
    public List<Bill> getAllBills() throws Exception {
        List<Bill> bills = new ArrayList<>();
        // Be explicit about columns (avoids surprises if schema changes)
        final String sql = "SELECT bill_id, accountNo, customer_name, bill_date, total_amount FROM bills ORDER BY bill_date DESC";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Bill b = new Bill();
                b.setBillId(rs.getInt("bill_id"));
                b.setAccountNo(rs.getInt("accountNo"));
                b.setCustomerName(rs.getString("customer_name"));
                b.setBillDate(rs.getTimestamp("bill_date"));
                b.setTotalAmount(rs.getDouble("total_amount"));
                b.setItems(new ArrayList<>());
                bills.add(b);
            }
        } finally {
            if (shouldClose(conn)) conn.close();
        }
        return bills;
    }

    @Override
    public Bill getBillById(int billId) throws Exception {
        Bill bill = null;

        final String sqlBill =
                "SELECT b.bill_id, b.accountNo, b.bill_date, b.customer_name, b.total_amount " +
                        "FROM bills b WHERE b.bill_id = ?";

        final String sqlItems =
                "SELECT item_id, bill_id, productNo, quantity " +
                        "FROM bill_items WHERE bill_id = ?";

        Connection conn = getConnection();
        try (PreparedStatement psBill = conn.prepareStatement(sqlBill)) {
            psBill.setInt(1, billId);
            try (ResultSet rs = psBill.executeQuery()) {
                if (rs.next()) {
                    bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setAccountNo(rs.getInt("accountNo"));
                    bill.setBillDate(rs.getTimestamp("bill_date"));
                    bill.setCustomerName(rs.getString("customer_name"));
                    bill.setTotalAmount(rs.getDouble("total_amount"));
                }
            }

            if (bill != null) {
                try (PreparedStatement psItems = conn.prepareStatement(sqlItems)) {
                    psItems.setInt(1, billId);
                    try (ResultSet rsItems = psItems.executeQuery()) {
                        List<BillItem> items = new ArrayList<>();
                        while (rsItems.next()) {
                            BillItem item = new BillItem();
                            item.setItemId(rsItems.getInt("item_id"));   // match your column names
                            item.setBillId(rsItems.getInt("bill_id"));
                            item.setProductNo(rsItems.getInt("productNo"));
                            item.setQuantity(rsItems.getInt("quantity"));
                            items.add(item);
                        }
                        bill.setItems(items);
                    }
                }
            }

        } finally {
            if (shouldClose(conn)) conn.close();
        }

        return bill;
    }

    @Override
    public String getCustomerNameByAccountNo(int accountNo) throws Exception {
        final String sql = "SELECT name FROM customers WHERE accountNo = ?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("name");
                return null;
            }
        } finally {
            if (shouldClose(conn)) conn.close();
        }
    }
}
