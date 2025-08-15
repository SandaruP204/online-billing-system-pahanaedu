package dao;

import model.BillItemDetails;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillItemDAO {

    private Connection testConn = null;

    // Production constructor
    public BillItemDAO() {}

    // Test constructor
    public BillItemDAO(Connection conn) {
        this.testConn = conn;
    }

    private Connection getConnection() throws Exception {
        if (testConn != null) return testConn;
        return DBConnection.getConnection();
    }

    public List<BillItemDetails> getBillItems(int billId) throws Exception {
        List<BillItemDetails> items = new ArrayList<>();

        String sql = "SELECT p.name AS productName, bi.quantity, p.price AS unitPrice, " +
                "(bi.quantity * p.price) AS total " +
                "FROM bill_items bi " +
                "JOIN products p ON bi.productNo = p.productNo " +
                "WHERE bi.bill_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
        }

        return items;
    }
}
