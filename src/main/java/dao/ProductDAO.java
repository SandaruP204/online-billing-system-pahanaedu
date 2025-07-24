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
            String sql = "INSERT INTO products (productNo, name, unit) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, product.getProductNo());
            ps.setString(2, product.getName());
            ps.setInt(3, product.getUnit());
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
                int no = rs.getInt("productNo"); // or "productNo"
                String name = rs.getString("name");
                int unit = rs.getInt("unit");

                System.out.println("DEBUG: Fetched row → No: " + no + ", Name: " + name + ", Unit: " + unit);

                Product product = new Product();
                product.setProductNo(no);
                product.setName(name);
                product.setUnit(unit);
                productList.add(product);
            }

            System.out.println("DEBUG: Final productList size: " + productList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

}
