package servlet;

import dao.CustomerDAO;
import model.Customer;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/AddCustomerServlet")
public class AddCustomerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int accountNo = Integer.parseInt(request.getParameter("accountNo"));
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        int units = Integer.parseInt(request.getParameter("unitsConsumed"));

        Customer customer = new Customer();
        customer.setAccountNo(accountNo);
        customer.setName(name);
        customer.setAddress(address);
        customer.setPhone(phone);
        customer.setUnitsConsumed(units);

        CustomerDAO dao = new CustomerDAO();
        dao.addCustomer(customer);

        response.sendRedirect("success.jsp");
    }
}
