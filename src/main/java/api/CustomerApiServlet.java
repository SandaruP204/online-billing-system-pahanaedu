package api;

import dao.CustomerDAO;
import model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/customers")
public class CustomerApiServlet extends HttpServlet {

    private CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String accountParam = request.getParameter("accountNo");

        if (accountParam != null) {
            // Get a single customer
            int accountNo = Integer.parseInt(accountParam);
            Customer c = customerDAO.getCustomer(accountNo);
            if (c != null) {
                response.getWriter().write(customerToJson(c));
            } else {
                response.getWriter().write("{\"error\":\"Customer not found\"}");
            }
        } else {
            // Get all customers
            List<Customer> customers = customerDAO.getAllCustomers();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < customers.size(); i++) {
                json.append(customerToJson(customers.get(i)));
                if (i < customers.size() - 1) json.append(",");
            }
            json.append("]");
            response.getWriter().write(json.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int accountNo = Integer.parseInt(request.getParameter("accountNo"));
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        int unitsConsumed = Integer.parseInt(request.getParameter("unitsConsumed"));

        Customer customer = new Customer();
        customer.setAccountNo(accountNo);
        customer.setName(name);
        customer.setAddress(address);
        customer.setPhone(phone);
        customer.setUnitsConsumed(unitsConsumed);

        customerDAO.addCustomer(customer);

        response.setContentType("application/json");
        response.getWriter().write("{\"success\":true}");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int accountNo = Integer.parseInt(request.getParameter("accountNo"));
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        int unitsConsumed = Integer.parseInt(request.getParameter("unitsConsumed"));

        Customer customer = new Customer();
        customer.setAccountNo(accountNo);
        customer.setName(name);
        customer.setAddress(address);
        customer.setPhone(phone);
        customer.setUnitsConsumed(unitsConsumed);

        customerDAO.updateCustomer(customer);

        response.setContentType("application/json");
        response.getWriter().write("{\"success\":true}");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int accountNo = Integer.parseInt(request.getParameter("accountNo"));
        customerDAO.deleteCustomer(accountNo);

        response.setContentType("application/json");
        response.getWriter().write("{\"success\":true}");
    }

    // Helper to convert a Customer object to JSON
    private String customerToJson(Customer c) {
        return "{"
                + "\"accountNo\":" + c.getAccountNo() + ","
                + "\"name\":\"" + c.getName() + "\","
                + "\"address\":\"" + c.getAddress() + "\","
                + "\"phone\":\"" + c.getPhone() + "\","
                + "\"unitsConsumed\":" + c.getUnitsConsumed()
                + "}";
    }
}
