package servlet;

import dao.BillDAO;
import dao.impl.BillDAOImpl;
import model.Bill;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/viewBills")
public class ViewBillsServlet extends HttpServlet {

    private BillDAO billDAO;

    @Override
    public void init() throws ServletException {
        this.billDAO = new BillDAOImpl(); // program to interface, instantiate impl
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Bill> billList = billDAO.getAllBills();

            // No need to loop and fetch names one-by-one:
            // getAllBills() already selects customer_name.

            request.setAttribute("bills", billList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("view_bills.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?msg=Failed to retrieve bills.");
        }
    }
}
