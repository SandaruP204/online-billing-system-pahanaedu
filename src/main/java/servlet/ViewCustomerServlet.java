package servlet;

import dao.CustomerDAO;
import model.Customer;
import java.util.List;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/viewCustomers")
public class ViewCustomerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CustomerDAO dao = new CustomerDAO();
        List<Customer> customers = dao.getAllCustomers();

        request.setAttribute("customers", customers);
        request.getRequestDispatcher("viewCustomers.jsp").forward(request, response);
    }
}
