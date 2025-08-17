package dao.impl;

import dao.ProductDAO;
import dao.DBConnection;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOimpl implements ProductDAO {

    @Override
    public boolean exists(int productNo) throws Exception {
        final String sql = "SELECT 1 FROM products WHERE productNo = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public Product findById(int productNo) throws Exception {
        final String sql = "SELECT productNo, name, unit, price FROM products WHERE productNo = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Product p = new Product();
                p.setProductNo(rs.getInt("productNo"));
                p.setName(rs.getString("name"));
                p.setUnit(rs.getInt("unit"));
                p.setPrice(rs.getDouble("price"));
                return p;
            }
        }
    }

    @Override
    public void addProduct(Product product) throws Exception {
        final String sql = "INSERT INTO products (productNo, name, unit, price) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, product.getProductNo());
            ps.setString(2, product.getName());
            ps.setInt(3, product.getUnit());
            ps.setDouble(4, product.getPrice());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateProduct(Product product) throws Exception {
        final String sql = "UPDATE products SET name = ?, unit = ?, price = ? WHERE productNo = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setInt(2, product.getUnit());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getProductNo());
            int updated = ps.executeUpdate();
            if (updated == 0) throw new IllegalArgumentException("Product not found: " + product.getProductNo());
        }
    }

    @Override
    public void deleteProduct(int productNo) throws Exception {
        final String sql = "DELETE FROM products WHERE productNo = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productNo);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Product> getAllProducts() throws Exception {
        final String sql = "SELECT productNo, name, unit, price FROM products ORDER BY productNo";
        List<Product> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductNo(rs.getInt("productNo"));
                p.setName(rs.getString("name"));
                p.setUnit(rs.getInt("unit"));
                p.setPrice(rs.getDouble("price"));
                list.add(p);
            }
        }
        return list;
    }

    @Override
    public double getPrice(int productNo) throws Exception {
        final String sql = "SELECT price FROM products WHERE productNo = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("price");
                throw new IllegalArgumentException("Product not found: " + productNo);
            }
        }
    }

    @Override
    public boolean decrementStock(int productNo, int qty) throws Exception {
        final String sql = "UPDATE products SET unit = unit - ? WHERE productNo = ? AND unit >= ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, productNo);
            ps.setInt(3, qty);
            int updated = ps.executeUpdate();
            return updated > 0;
        }
    }

    @Override
    public void updateProductStock(int productNo, int newUnit) throws Exception {
        final String sql = "UPDATE products SET unit = ? WHERE productNo = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, newUnit);
            ps.setInt(2, productNo);
            ps.executeUpdate();
        }
    }
}
