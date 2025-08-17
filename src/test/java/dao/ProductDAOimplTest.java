package dao;

import dao.impl.ProductDAOimpl;
import model.Product;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDAOimplTest {

    private ProductDAO productDAO;
    private static final int TEST_PRODUCT_NO = 900001; // unlikely to collide

    @BeforeAll
    void setUp() throws Exception {
        productDAO = new ProductDAOimpl();
        // ensure clean start
        try { productDAO.deleteProduct(TEST_PRODUCT_NO); } catch (Exception ignored) {}
    }

    @AfterAll
    void tearDown() throws Exception {
        try { productDAO.deleteProduct(TEST_PRODUCT_NO); } catch (Exception ignored) {}
    }

    @Test
    void addFetchUpdateDeleteProduct() throws Exception {
        // Add
        Product p = new Product();
        p.setProductNo(TEST_PRODUCT_NO);
        p.setName("JUnit Temp Product");
        p.setUnit(10);
        p.setPrice(123.45);
        productDAO.addProduct(p);

        // Fetch
        Product fetched = productDAO.findById(TEST_PRODUCT_NO);
        assertNotNull(fetched, "Product should be inserted and retrievable");
        assertEquals(TEST_PRODUCT_NO, fetched.getProductNo());
        assertEquals("JUnit Temp Product", fetched.getName());
        assertEquals(10, fetched.getUnit());
        assertEquals(123.45, fetched.getPrice(), 1e-6);

        // Update fields
        fetched.setName("JUnit Temp Product v2");
        fetched.setUnit(11);
        fetched.setPrice(150.00);
        productDAO.updateProduct(fetched);

        // Verify update
        Product afterUpdate = productDAO.findById(TEST_PRODUCT_NO);
        assertNotNull(afterUpdate);
        assertEquals("JUnit Temp Product v2", afterUpdate.getName());
        assertEquals(11, afterUpdate.getUnit());
        assertEquals(150.00, afterUpdate.getPrice(), 1e-6);

        // Update stock only
        productDAO.updateProductStock(TEST_PRODUCT_NO, 3);
        Product afterStock = productDAO.findById(TEST_PRODUCT_NO);
        assertNotNull(afterStock);
        assertEquals(3, afterStock.getUnit());

        // Delete
        productDAO.deleteProduct(TEST_PRODUCT_NO);
        Product afterDelete = productDAO.findById(TEST_PRODUCT_NO);
        assertNull(afterDelete, "Product should be deleted");
    }
}
