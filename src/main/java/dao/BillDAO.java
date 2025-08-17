package dao;

import model.Bill;
import java.util.List;

public interface BillDAO {
    int addBill(Bill bill) throws Exception;

    // NOTE: you currently call this from AddBillServlet, so it stays in the interface
    double getProductPrice(int productNo) throws Exception;

    List<Bill> getAllBills() throws Exception;

    Bill getBillById(int billId) throws Exception;

    String getCustomerNameByAccountNo(int accountNo) throws Exception;
}
