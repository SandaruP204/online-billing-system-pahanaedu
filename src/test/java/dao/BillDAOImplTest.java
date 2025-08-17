package dao;

import dao.impl.BillDAOImpl;
import model.Bill;
import model.BillItem;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BillDAOImplTest {

    private Connection testConn;
    private BillDAOImpl billDAOImpl;

    @BeforeAll
    public void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        testConn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/pahanaedu",
                "root", ""
        );
        billDAOImpl = new BillDAOImpl(testConn);
    }

    @AfterAll
    public void cleanup() throws Exception {
        if (testConn != null && !testConn.isClosed()) testConn.close();
    }

    @Test
    public void testAddAndFetchBill() throws Exception {
        Bill bill = new Bill();
        bill.setAccountNo(1);
        bill.setBillDate(new java.util.Date());

        List<BillItem> items = new ArrayList<>();
        items.add(new BillItem(0, 0, 1, 2)); // productNo=1, quantity=2
        bill.setItems(items);

        int billId = billDAOImpl.addBill(bill);
        Assertions.assertTrue(billId > 0);

        Bill fetched = billDAOImpl.getBillById(billId);
        Assertions.assertNotNull(fetched);
        Assertions.assertEquals(billId, fetched.getBillId());

        // check items list initialized
        Assertions.assertNotNull(fetched.getItems());
    }
}
