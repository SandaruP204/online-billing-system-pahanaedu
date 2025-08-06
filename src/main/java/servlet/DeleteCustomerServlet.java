package servlet;

import dao.CustomerDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/DeleteCustomerServlet")
public class DeleteCustomerServlet extends HttpServlet {

    // Handles POST requests (e.g., from a form)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int accountNo = Integer.parseInt(request.getParameter("accountNo"));
        CustomerDAO dao = new CustomerDAO();
        dao.deleteCustomer(accountNo);
        response.sendRedirect("ViewCustomerServlet");
    }

    // Handles GET requests (e.g., from an <a href="..."> link)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response); // Just call doPost and reuse the logic
    }
}
