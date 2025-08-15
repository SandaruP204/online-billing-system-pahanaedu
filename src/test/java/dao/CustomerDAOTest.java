package dao;

import model.Customer;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerDAOTest {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private static final int TEST_ACCOUNT_NO = 900001; // unlikely to collide

    @BeforeAll
    void cleanStart() {
        try { customerDAO.deleteCustomer(TEST_ACCOUNT_NO); } catch (Exception ignored) {}
    }

    @AfterAll
    void cleanEnd() {
        try { customerDAO.deleteCustomer(TEST_ACCOUNT_NO); } catch (Exception ignored) {}
    }

    @Test
    void addFetchUpdateDeleteCustomer() {
        // Add
        Customer c = new Customer();
        c.setAccountNo(TEST_ACCOUNT_NO);
        c.setName("JUnit Temp Customer");
        c.setAddress("123 Test St");
        c.setPhone("0771234567");
        c.setUnitsConsumed(42);
        customerDAO.addCustomer(c);

        // Fetch
        Customer fetched = customerDAO.getCustomer(TEST_ACCOUNT_NO);
        assertNotNull(fetched, "Customer should be inserted and retrievable");
        assertEquals(TEST_ACCOUNT_NO, fetched.getAccountNo());
        assertEquals("JUnit Temp Customer", fetched.getName());
        assertEquals("123 Test St", fetched.getAddress());
        assertEquals("0771234567", fetched.getPhone());
        assertEquals(42, fetched.getUnitsConsumed());

        // Update
        fetched.setName("JUnit Temp Customer v2");
        fetched.setAddress("456 Updated Ave");
        fetched.setPhone("0717654321");
        fetched.setUnitsConsumed(55);
        customerDAO.updateCustomer(fetched);

        // Verify update
        Customer afterUpdate = customerDAO.getCustomer(TEST_ACCOUNT_NO);
        assertNotNull(afterUpdate);
        assertEquals("JUnit Temp Customer v2", afterUpdate.getName());
        assertEquals("456 Updated Ave", afterUpdate.getAddress());
        assertEquals("0717654321", afterUpdate.getPhone());
        assertEquals(55, afterUpdate.getUnitsConsumed());

        // List all contains our record
        List<Customer> all = customerDAO.getAllCustomers();
        assertTrue(all.stream().anyMatch(x -> x.getAccountNo() == TEST_ACCOUNT_NO));

        // Delete
        customerDAO.deleteCustomer(TEST_ACCOUNT_NO);
        Customer afterDelete = customerDAO.getCustomer(TEST_ACCOUNT_NO);
        assertNull(afterDelete, "Customer should be deleted");
    }
}
