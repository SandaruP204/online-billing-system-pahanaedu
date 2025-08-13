package dao;

import model.Bill;
import model.BillItem;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillDAOTest {

    private static BillDAO billDAO;

    @BeforeAll
    static void setup() {
        billDAO = new BillDAO();
    }

    @Test
    void testAddAndFetchBill() throws Exception {
        // 1️⃣ Prepare bill items
        List<BillItem> items = new ArrayList<>();
        BillItem item1 = new BillItem();
        item1.setProductNo(1);  // make sure productNo 1 exists in DB
        item1.setQuantity(1);
        items.add(item1);

        // 2️⃣ Create bill
        Bill bill = new Bill();
        bill.setAccountNo(1);  // make sure accountNo 1 exists
        bill.setBillDate(new Date());
        bill.setItems(items);

        // 3️⃣ Add bill
        int generatedBillId = billDAO.addBill(bill);
        assertTrue(generatedBillId > 0, "Generated bill ID should be positive");

        // 4️⃣ Fetch bill by ID
        Bill fetchedBill = billDAO.getBillById(generatedBillId);
        assertNotNull(fetchedBill, "Fetched bill should not be null");
        assertEquals(bill.getAccountNo(), fetchedBill.getAccountNo(), "AccountNo should match");
        assertEquals(items.size(), fetchedBill.getItems() != null ? fetchedBill.getItems().size() : 0, "Item count should match");
    }

    @Test
    void testGetAllBills() throws Exception {
        List<Bill> bills = billDAO.getAllBills();
        assertNotNull(bills, "List of bills should not be null");
        assertTrue(bills.size() >= 0, "Bill list size should be 0 or more");
    }
}
