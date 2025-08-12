package servlet;

import dao.BillDAO;
import dao.BillItemDAO;
import model.Bill;
import model.BillItemDetails;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/viewBill")
public class ViewSingleBillServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int billId = Integer.parseInt(request.getParameter("billId"));

            BillDAO billDAO = new BillDAO();
            Bill bill = billDAO.getBillById(billId);

            BillItemDAO itemDAO = new BillItemDAO();
            List<BillItemDetails> billItems = itemDAO.getBillItems(billId);

            request.setAttribute("bill", bill);
            request.setAttribute("billItems", billItems);

            RequestDispatcher dispatcher = request.getRequestDispatcher("viewSingleBill.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?msg=Failed to retrieve bill details.");
        }
    }


}
