package dao;

import model.Bill;
import model.BillItemDetails;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillItemDAO {

    public List<BillItemDetails> getBillItems(int billId) throws SQLException {
        List<BillItemDetails> items = new ArrayList<>();

        String sql = "SELECT p.name AS productName, bi.quantity, p.price AS unitPrice, " +
                "(bi.quantity * p.price) AS total " +
                "FROM bill_items bi " +
                "JOIN products p ON bi.productNo = p.productNo " +
                "WHERE bi.bill_id = ?";

        try (Connection conn = DBConnection.getConnection();
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




    // 🎯 Main method to quickly test
    public static void main(String[] args) {
        BillItemDAO dao = new BillItemDAO();
        try {
            List<BillItemDetails> items = dao.getBillItems(1); // test with billId = 1
            for (BillItemDetails item : items) {
                System.out.println("Product: " + item.getProductName());
                System.out.println("Quantity: " + item.getQuantity());
                System.out.println("Unit Price: " + item.getUnitPrice());
                System.out.println("Total: " + item.getTotal());
                System.out.println("----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
