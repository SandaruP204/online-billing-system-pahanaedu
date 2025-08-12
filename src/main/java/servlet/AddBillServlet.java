package servlet;

import dao.BillDAO;
import model.Bill;
import model.BillItem;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/addBill")
public class AddBillServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int accountNo = Integer.parseInt(request.getParameter("accountNo"));

            // Assuming items are submitted as arrays (e.g., productNo[], quantity[])
            String[] productNos = request.getParameterValues("productNo");
            String[] quantities = request.getParameterValues("quantity");

            List<BillItem> items = new ArrayList<>();
            if (productNos != null && quantities != null) {
                for (int i = 0; i < productNos.length; i++) {
                    int productNo = Integer.parseInt(productNos[i]);
                    int quantity = Integer.parseInt(quantities[i]);
                    items.add(new BillItem(0, 0, productNo, quantity));
                }
            }

            Bill bill = new Bill();
            bill.setAccountNo(accountNo);
            bill.setBillDate(new Date());
            bill.setItems(items);

            BillDAO billDAO = new BillDAO();
            int billId = billDAO.addBill(bill);

            if (billId != -1) {
                response.sendRedirect("success.jsp?msg=Bill created successfully. Bill ID: " + billId);
            } else {
                // More informative error message
                response.setContentType("text/plain");
                response.getWriter().write("Failed to create bill for unknown reasons.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("Error occurred while creating bill: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect to bill form page if accessed directly via GET
        response.sendRedirect("addBill.jsp");
    }
}
