package dao;

import model.Bill;
import model.BillItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    private Connection testConn = null;

    public BillDAO() {}
    public BillDAO(Connection conn) { this.testConn = conn; }

    private Connection getConnection() throws Exception {
        if (testConn != null) return testConn;
        return DBConnection.getConnection();
    }

    private boolean shouldClose(Connection conn) {
        return testConn == null && conn != null;
    }

    public int addBill(Bill bill) throws Exception {
        int generatedBillId = -1;

        // Initialize items list if null
        if (bill.getItems() == null) bill.setItems(new ArrayList<>());

        double totalAmount = 0;
        for (BillItem item : bill.getItems()) {
            totalAmount += getProductPrice(item.getProductNo()) * item.getQuantity();
        }

        String getCustomerSQL = "SELECT name FROM customers WHERE accountNo = ?";
        String insertBillSQL = "INSERT INTO bills (accountNo, customer_name, bill_date, total_amount) VALUES (?, ?, ?, ?)";
        String insertItemSQL = "INSERT INTO bill_items (bill_id, productNo, quantity) VALUES (?, ?, ?)";
        String checkProductSQL = "SELECT unit FROM products WHERE productNo = ?";
        String updateStockSQL = "UPDATE products SET unit = unit - ? WHERE productNo = ?";

        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);

            // Get customer name
            String customerName;
            try (PreparedStatement ps = conn.prepareStatement(getCustomerSQL)) {
                ps.setInt(1, bill.getAccountNo());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) customerName = rs.getString("name");
                    else throw new Exception("Customer does not exist");
                }
            }

            // Validate stock
            for (BillItem item : bill.getItems()) {
                try (PreparedStatement ps = conn.prepareStatement(checkProductSQL)) {
                    ps.setInt(1, item.getProductNo());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next() && rs.getInt("unit") < item.getQuantity()) {
                            throw new Exception("Not enough stock for product " + item.getProductNo());
                        }
                    }
                }
            }

            // Insert bill
            try (PreparedStatement ps = conn.prepareStatement(insertBillSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, bill.getAccountNo());
                ps.setString(2, customerName);
                ps.setTimestamp(3, new Timestamp(bill.getBillDate().getTime()));
                ps.setDouble(4, totalAmount);
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) generatedBillId = keys.getInt(1);
                }
            }

            bill.setCustomerName(customerName);
            bill.setTotalAmount(totalAmount);

            // Insert items & update stock
            try (PreparedStatement psItem = conn.prepareStatement(insertItemSQL);
                 PreparedStatement psStock = conn.prepareStatement(updateStockSQL)) {
                for (BillItem item : bill.getItems()) {
                    psItem.setInt(1, generatedBillId);
                    psItem.setInt(2, item.getProductNo());
                    psItem.setInt(3, item.getQuantity());
                    psItem.addBatch();

                    psStock.setInt(1, item.getQuantity());
                    psStock.setInt(2, item.getProductNo());
                    psStock.addBatch();
                }
                psItem.executeBatch();
                psStock.executeBatch();
            }

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (shouldClose(conn)) conn.close();
        }

        return generatedBillId;
    }

    public double getProductPrice(int productNo) throws Exception {
        double price = 0;
        String sql = "SELECT price FROM products WHERE productNo = ?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) price = rs.getDouble("price");
                else throw new Exception("Product not found");
            }
        } finally {
            if (shouldClose(conn)) conn.close();
        }
        return price;
    }

    public List<Bill> getAllBills() throws Exception {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Bill b = new Bill();
                b.setBillId(rs.getInt("bill_id"));
                b.setAccountNo(rs.getInt("accountNo"));
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

    public Bill getBillById(int billId) throws Exception {
        Bill bill = null;
        String sqlBill = "SELECT b.bill_id, b.accountNo, b.bill_date, c.name AS customer_name, b.total_amount " +
                "FROM bills b JOIN customers c ON b.accountNo = c.accountNo WHERE b.bill_id = ?";

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

            // Fetch bill items
            if (bill != null) {
                String sqlItems = "SELECT * FROM bill_items WHERE bill_id = ?";
                try (PreparedStatement psItems = conn.prepareStatement(sqlItems)) {
                    psItems.setInt(1, billId);
                    try (ResultSet rsItems = psItems.executeQuery()) {
                        List<BillItem> items = new ArrayList<>();
                        while (rsItems.next()) {
                            BillItem item = new BillItem();
                            item.setItemId(rsItems.getInt("item_id"));  // ← match your DB column
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

    public String getCustomerNameByAccountNo(int accountNo) throws Exception {
        String name = null;
        String sql = "SELECT name FROM customers WHERE accountNo = ?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) name = rs.getString("name");
            }
        } finally {
            if (shouldClose(conn)) conn.close();
        }
        return name;
    }
}
