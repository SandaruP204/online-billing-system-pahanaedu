package dao;

import model.Product;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDAOTest {

    private final ProductDAO productDAO = new ProductDAO();
    private static final int TEST_PRODUCT_NO = 900001; // unlikely to collide

    @BeforeAll
    void cleanStart() {
        // ensure no leftover row from a previous run
        try { productDAO.deleteProduct(TEST_PRODUCT_NO); } catch (Exception ignored) {}
    }

    @AfterAll
    void cleanEnd() {
        try { productDAO.deleteProduct(TEST_PRODUCT_NO); } catch (Exception ignored) {}
    }

    @Test
    void addFetchUpdateDeleteProduct() {
        // Add
        Product p = new Product();
        p.setProductNo(TEST_PRODUCT_NO);
        p.setName("JUnit Temp Product");
        p.setUnit(10);
        p.setPrice(123.45);
        productDAO.addProduct(p); // void method

        // Fetch
        Product fetched = ProductDAO.getProductByNo(TEST_PRODUCT_NO); // static
        assertNotNull(fetched, "Product should be inserted and retrievable");
        assertEquals(TEST_PRODUCT_NO, fetched.getProductNo());
        assertEquals("JUnit Temp Product", fetched.getName());
        assertEquals(10, fetched.getUnit());
        assertEquals(123.45, fetched.getPrice(), 0.0001);

        // Update fields
        fetched.setName("JUnit Temp Product v2");
        fetched.setUnit(11);
        fetched.setPrice(150.00);
        productDAO.updateProduct(fetched);

        // Verify update
        Product afterUpdate = ProductDAO.getProductByNo(TEST_PRODUCT_NO);
        assertNotNull(afterUpdate);
        assertEquals("JUnit Temp Product v2", afterUpdate.getName());
        assertEquals(11, afterUpdate.getUnit());
        assertEquals(150.00, afterUpdate.getPrice(), 0.0001);

        // Update stock only
        productDAO.updateProductStock(TEST_PRODUCT_NO, 3);
        Product afterStock = ProductDAO.getProductByNo(TEST_PRODUCT_NO);
        assertNotNull(afterStock);
        assertEquals(3, afterStock.getUnit());

        // Delete
        productDAO.deleteProduct(TEST_PRODUCT_NO);
        Product afterDelete = ProductDAO.getProductByNo(TEST_PRODUCT_NO);
        assertNull(afterDelete, "Product should be deleted");
    }
}
