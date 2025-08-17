package dao.impl;

import dao.BillItemDAO;
import dao.DBConnection;
import model.BillItem;
import model.BillItemDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillItemDAOimpl implements BillItemDAO {

    private Connection testConn = null;

    // Production constructor
    public BillItemDAOimpl() {}

    // Test constructor (inject a connection you control)
    public BillItemDAOimpl(Connection conn) {
        this.testConn = conn;
    }

    private Connection getConnection() throws Exception {
        if (testConn != null) return testConn;
        return DBConnection.getConnection();
    }

    private boolean shouldClose(Connection conn) {
        return testConn == null && conn != null;
    }

    @Override
    public List<BillItemDetails> getBillItems(int billId) throws Exception {
        List<BillItemDetails> items = new ArrayList<>();

        final String sql =
                "SELECT p.name AS productName, bi.quantity, p.price AS unitPrice, " +
                        "       (bi.quantity * p.price) AS total " +
                        "FROM bill_items bi " +
                        "JOIN products p ON bi.productNo = p.productNo " +
                        "WHERE bi.bill_id = ?";

        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BillItemDetails item = new BillItemDetails();
                    item.setProductName(rs.getString("productName"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getDouble("unitPrice"));
                    item.setTotal(rs.getDouble("total"));
                    items.add(item);
                }
            }
        } finally {
            if (shouldClose(conn)) conn.close();
        }

        return items;
    }

    @Override
    public void addItems(int billId, List<BillItem> items) throws Exception {
        if (items == null || items.isEmpty()) return;

        final String sql = "INSERT INTO bill_items (bill_id, productNo, quantity) VALUES (?,?,?)";

        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (BillItem it : items) {
                if (it == null) continue;
                if (it.getProductNo() <= 0) {
                    throw new IllegalArgumentException("Invalid productNo in bill item.");
                }
                if (it.getQuantity() <= 0) {
                    throw new IllegalArgumentException("Quantity must be > 0 for product " + it.getProductNo());
                }

                ps.setInt(1, billId);
                ps.setInt(2, it.getProductNo());
                ps.setInt(3, it.getQuantity());
                ps.addBatch();
            }
            ps.executeBatch();
        } finally {
            if (shouldClose(conn)) conn.close();
        }
    }
}
