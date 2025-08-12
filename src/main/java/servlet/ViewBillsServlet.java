package servlet;

import dao.BillDAO;
import model.Bill;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/viewBills")
public class ViewBillsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            BillDAO billDAO = new BillDAO();
            List<Bill> billList = billDAO.getAllBills();

            request.setAttribute("bills", billList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("view_bills.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?msg=Failed to retrieve bills.");
        }
    }
}
