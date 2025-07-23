package dao;

import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductDAO {
    public void addProduct(Product product) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO products (prroductNo, name, unit) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, products.getAccountNo());
            ps.setString(2, products.getName());
            ps.setInt(5, products.getUnit());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Product getProductNo(int productNo) {
        Product product = null;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM products WHERE prroductNo = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, productNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setProductNo(rs.getInt("productNo"));
                product.setName(rs.getString("name"));
                product.setUnit(rs.getInt("unit"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }
}
