package servlet;

import dao.CustomerDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Customer;

import java.io.IOException;

@WebServlet("/UpdateCustomerServlet")
public class UpdateCustomerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int accountNo = Integer.parseInt(request.getParameter("accountNo"));
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        int units = Integer.parseInt(request.getParameter("unitsConsumed"));

        Customer customer = new Customer(accountNo, name, address, phone, units);
        CustomerDAO dao = new CustomerDAO();
        dao.updateCustomer(customer);

        response.sendRedirect("ViewCustomerServlet");
    }
}
