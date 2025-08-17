package servlet;

import dao.BillDAO;
import dao.impl.BillDAOImpl;
import dao.impl.BillItemDAOimpl;          // keeping your existing item DAO as-is
import model.Bill;
import model.BillItemDetails;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/viewBill")
public class ViewSingleBillServlet extends HttpServlet {

    private BillDAO billDAO;
    private BillItemDAOimpl itemDAO;

    @Override
    public void init() throws ServletException {
        this.billDAO = new BillDAOImpl();  // program to interface, instantiate impl
        this.itemDAO = new BillItemDAOimpl();  // your existing concrete DAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String billIdStr = request.getParameter("billId");
        if (billIdStr == null || billIdStr.isBlank()) {
            response.sendRedirect("error.jsp?msg=Missing billId.");
            return;
        }

        try {
            int billId = Integer.parseInt(billIdStr);

            Bill bill = billDAO.getBillById(billId);
            if (bill == null) {
                // Not found → friendly error
                response.sendRedirect("error.jsp?msg=Bill not found.");
                return;
            }

            // If your DAO already populated customer_name, this is a no-op.
            // Otherwise, fall back to a lookup by accountNo.
            if (bill.getCustomerName() == null || bill.getCustomerName().isBlank()) {
                String name = billDAO.getCustomerNameByAccountNo(bill.getAccountNo());
                bill.setCustomerName(name);
            }

            List<BillItemDetails> billItems = itemDAO.getBillItems(billId);

            request.setAttribute("bill", bill);
            request.setAttribute("items", billItems);

            RequestDispatcher dispatcher = request.getRequestDispatcher("viewSingleBill.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect("error.jsp?msg=Invalid billId.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?msg=Failed to retrieve bill details.");
        }
    }
}
