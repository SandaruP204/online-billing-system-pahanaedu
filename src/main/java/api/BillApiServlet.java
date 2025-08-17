package api;

import dao.impl.BillDAOImpl;
import model.Bill;
import model.BillItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/api/bills")
public class BillApiServlet extends HttpServlet {

    private BillDAOImpl billDAOImpl = new BillDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String billIdParam = request.getParameter("billId");
            StringBuilder json = new StringBuilder();

            if (billIdParam != null) {
                int billId = Integer.parseInt(billIdParam);
                Bill bill = billDAOImpl.getBillById(billId);
                if (bill != null) {
                    json.append("{")
                            .append("\"billId\":").append(bill.getBillId()).append(",")
                            .append("\"accountNo\":").append(bill.getAccountNo()).append(",")
                            .append("\"customerName\":\"").append(bill.getCustomerName()).append("\",")
                            .append("\"billDate\":\"").append(new SimpleDateFormat("yyyy-MM-dd").format(bill.getBillDate())).append("\",")
                            .append("\"totalAmount\":").append(bill.getTotalAmount())
                            .append("}");
                } else {
                    json.append("{}");
                }
            } else {
                List<Bill> bills = billDAOImpl.getAllBills();
                json.append("[");
                for (int i = 0; i < bills.size(); i++) {
                    Bill b = bills.get(i);
                    json.append("{")
                            .append("\"billId\":").append(b.getBillId()).append(",")
                            .append("\"accountNo\":").append(b.getAccountNo()).append(",")
                            .append("\"billDate\":\"").append(new SimpleDateFormat("yyyy-MM-dd").format(b.getBillDate())).append("\",")
                            .append("\"totalAmount\":").append(b.getTotalAmount())
                            .append("}");
                    if (i < bills.size() - 1) json.append(",");
                }
                json.append("]");
            }

            response.getWriter().write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"Failed to fetch bills\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int accountNo = Integer.parseInt(request.getParameter("accountNo"));
            String dateStr = request.getParameter("billDate"); // format yyyy-MM-dd
            Date billDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

            // Expecting a JSON-like param for items, or you can send multiple params like item1_productNo, item1_quantity, etc.
            // For simplicity, here we create an empty list — you can adapt later
            List<BillItem> items = new ArrayList<>();

            Bill bill = new Bill();
            bill.setAccountNo(accountNo);
            bill.setBillDate(billDate);
            bill.setItems(items);

            int generatedBillId = billDAOImpl.addBill(bill);

            response.setContentType("application/json");
            response.getWriter().write("{\"success\":true,\"billId\":" + generatedBillId + "}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
