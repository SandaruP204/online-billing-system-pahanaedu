// src/main/java/dao/ProductDAO.java
package dao;

import model.Product;
import java.util.List;

public interface ProductDAO {

    // ✅ check if product exists
    boolean exists(int productNo) throws Exception;

    // ✅ fetch single product by ID
    Product findById(int productNo) throws Exception;

    // ✅ insert new product
    void addProduct(Product p) throws Exception;

    // ✅ update existing product
    void updateProduct(Product p) throws Exception;

    // ✅ delete by ID
    void deleteProduct(int productNo) throws Exception;

    // ✅ list all products
    List<Product> getAllProducts() throws Exception;

    // ✅ return only price
    double getPrice(int productNo) throws Exception;

    // ✅ decrease stock atomically (e.g. during billing)
    boolean decrementStock(int productNo, int qty) throws Exception;

    // ✅ update only stock directly
    void updateProductStock(int productNo, int newUnit) throws Exception;
}
