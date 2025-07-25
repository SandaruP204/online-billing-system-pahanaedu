package dao;

import model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public void addProduct(Product product) {
        try {
            Connection con = DBConnection.getConnection();
            // ✅ FIX: corrected table & columns, use commas correctly!
            String sql = "INSERT INTO products (productNo, name, unit, price) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, product.getProductNo());
            ps.setString(2, product.getName());
            ps.setInt(3, product.getUnit());
            ps.setDouble(4, product.getPrice()); // ✅ Correct index!
            ps.executeUpdate();
            System.out.println("Product added!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            System.out.println("DEBUG: DB Connection OK");

            String sql = "SELECT * FROM products";
            System.out.println("DEBUG: Running query: " + sql);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int no = rs.getInt("productNo");
                String name = rs.getString("name");
                int unit = rs.getInt("unit");
                double price = rs.getDouble("price");

                System.out.println("DEBUG: Fetched → No: " + no + ", Name: " + name + ", Unit: " + unit + ", Price: " + price);

                Product product = new Product();
                product.setProductNo(no);
                product.setName(name);
                product.setUnit(unit);
                product.setPrice(price);
                productList.add(product);
            }

            System.out.println("DEBUG: Final productList size: " + productList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    public Product getProductByNo(int productNo) {
        Product product = null;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM products WHERE productNo = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, productNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setProductNo(rs.getInt("productNo"));
                product.setName(rs.getString("name"));
                product.setUnit(rs.getInt("unit"));
                product.setPrice(rs.getDouble("price"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    public void updateProductStock(int productNo, int newUnit) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE products SET unit = ? WHERE productNo = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, newUnit);
            ps.setInt(2, productNo);
            ps.executeUpdate();
            System.out.println("Stock updated: Product " + productNo + " new unit: " + newUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
