package dao;

import model.Bill;
import model.BillItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillDAO {

    // Optional connection for testing
    private Connection testConn = null;

    // Production constructor
    public BillDAO() {}

    // Test constructor
    public BillDAO(Connection conn) {
        this.testConn = conn;
    }

    // Internal method to get connection
    private Connection getConnection() throws Exception {
        if (testConn != null) return testConn;
        return DBConnection.getConnection();
    }

    public int addBill(Bill bill) throws Exception {
        int generatedBillId = -1;

        double totalAmount = 0;
        for (BillItem item : bill.getItems()) {
            double price = getProductPrice(item.getProductNo());
            totalAmount += price * item.getQuantity();
        }

        String getCustomerNameSQL = "SELECT name FROM customers WHERE accountNo = ?";
        String insertBillSQL = "INSERT INTO bills (accountNo, customer_name, bill_date, total_amount) VALUES (?, ?, ?, ?)";
        String insertItemSQL = "INSERT INTO bill_items (bill_id, productNo, quantity) VALUES (?, ?, ?)";
        String checkProductSQL = "SELECT unit FROM products WHERE productNo = ?";
        String updateProductQtySQL = "UPDATE products SET unit = unit - ? WHERE productNo = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // Get customer name
            String customerName = "";
            try (PreparedStatement psGetName = conn.prepareStatement(getCustomerNameSQL)) {
                psGetName.setInt(1, bill.getAccountNo());
                try (ResultSet rs = psGetName.executeQuery()) {
                    if (rs.next()) {
                        customerName = rs.getString("name");
                    } else {
                        throw new Exception("Customer with accountNo " + bill.getAccountNo() + " does not exist.");
                    }
                }
            }

            // Validate product existence and stock availability
            for (BillItem item : bill.getItems()) {
                try (PreparedStatement psCheckProduct = conn.prepareStatement(checkProductSQL)) {
                    psCheckProduct.setInt(1, item.getProductNo());
                    try (ResultSet rs = psCheckProduct.executeQuery()) {
                        if (rs.next()) {
                            int stockQty = rs.getInt("unit");
                            if (stockQty < item.getQuantity()) {
                                throw new Exception("Not enough stock for productNo " + item.getProductNo() +
                                        ". Available: " + stockQty + ", requested: " + item.getQuantity());
                            }
                        } else {
                            throw new Exception("Product with productNo " + item.getProductNo() + " does not exist.");
                        }
                    }
                }
            }

            // Insert bill
            try (PreparedStatement psBill = conn.prepareStatement(insertBillSQL, Statement.RETURN_GENERATED_KEYS)) {
                psBill.setInt(1, bill.getAccountNo());
                psBill.setString(2, customerName);
                psBill.setTimestamp(3, new Timestamp(bill.getBillDate().getTime()));
                psBill.setDouble(4, totalAmount);
                int affectedRows = psBill.executeUpdate();

                if (affectedRows == 0) {
                    conn.rollback();
                    throw new Exception("Creating bill failed, no rows affected.");
                }

                try (ResultSet generatedKeys = psBill.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedBillId = generatedKeys.getInt(1);
                    } else {
                        conn.rollback();
                        throw new Exception("Creating bill failed, no ID obtained.");
                    }
                }
            }

            bill.setCustomerName(customerName);
            bill.setTotalAmount(totalAmount);

            // Insert items and update product quantities
            try (PreparedStatement psItem = conn.prepareStatement(insertItemSQL);
                 PreparedStatement psUpdateQty = conn.prepareStatement(updateProductQtySQL)) {

                for (BillItem item : bill.getItems()) {
                    psItem.setInt(1, generatedBillId);
                    psItem.setInt(2, item.getProductNo());
                    psItem.setInt(3, item.getQuantity());
                    psItem.addBatch();

                    psUpdateQty.setInt(1, item.getQuantity());
                    psUpdateQty.setInt(2, item.getProductNo());
                    psUpdateQty.addBatch();
                }

                psItem.executeBatch();
                psUpdateQty.executeBatch();
            }

            conn.commit();
        } catch (Exception e) {
            throw e;
        }

        return generatedBillId;
    }

    public double getProductPrice(int productNo) throws SQLException, Exception {
        double price = 0;
        String sql = "SELECT price FROM products WHERE productNo = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productNo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    price = rs.getDouble("price");
                } else {
                    throw new SQLException("Product with productNo " + productNo + " not found.");
                }
            }
        }
        return price;
    }

    public List<Bill> getAllBills() throws SQLException, Exception {
        List<Bill> billList = new ArrayList<>();
        String sql = "SELECT * FROM bills";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Bill bill = new Bill();
                bill.setBillId(rs.getInt("bill_id"));
                bill.setAccountNo(rs.getInt("accountNo"));
                bill.setBillDate(rs.getTimestamp("bill_date"));
                bill.setTotalAmount(rs.getDouble("total_amount"));
                billList.add(bill);
            }
        }

        return billList;
    }

    public Bill getBillById(int billId) throws SQLException, Exception {
        Bill bill = null;
        String sql = "SELECT b.bill_id, b.accountNo, b.bill_date, c.name AS customer_name, b.total_amount " +
                "FROM bills b JOIN customers c ON b.accountNo = c.accountNo WHERE b.bill_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, billId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setAccountNo(rs.getInt("accountNo"));
                    bill.setBillDate(rs.getTimestamp("bill_date"));
                    bill.setCustomerName(rs.getString("customer_name"));
                    bill.setTotalAmount(rs.getDouble("total_amount"));
                }
            }
        }

        return bill;
    }

    public String getCustomerNameByAccountNo(int accountNo) throws SQLException, Exception {
        String name = null;
        String sql = "SELECT name FROM customers WHERE accountNo = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountNo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                }
            }
        }

        return name;
    }
}
