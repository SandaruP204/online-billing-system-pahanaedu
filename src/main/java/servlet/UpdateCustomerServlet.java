package servlet;

import dao.CustomerDAO;
import dao.impl.CustomerDAOimpl;
import model.Customer;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/UpdateCustomerServlet")
public class UpdateCustomerServlet extends HttpServlet {

    private CustomerDAO customerDAO;

    @Override
    public void init() {
        this.customerDAO = new CustomerDAOimpl(); // program to the interface
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String accountNoStr = request.getParameter("accountNo");
            String name         = request.getParameter("name");
            String address      = request.getParameter("address");
            String phone        = request.getParameter("phone");
            String unitsStr     = request.getParameter("unitsConsumed");

            if (accountNoStr == null || accountNoStr.isBlank()
                    || name == null || name.isBlank()
                    || address == null || address.isBlank()
                    || phone == null || phone.isBlank()
                    || unitsStr == null || unitsStr.isBlank()) {
                response.sendRedirect("ViewCustomerServlet?msg=All fields are required");
                return;
            }

            int accountNo = Integer.parseInt(accountNoStr);
            int units     = Integer.parseInt(unitsStr);

            if (accountNo <= 0) {
                response.sendRedirect("ViewCustomerServlet?msg=Invalid account number");
                return;
            }
            if (units < 0) {
                response.sendRedirect("ViewCustomerServlet?msg=Units consumed cannot be negative");
                return;
            }

            Customer customer = new Customer(accountNo, name.trim(), address.trim(), phone.trim(), units);
            customerDAO.updateCustomer(customer);

            response.sendRedirect("ViewCustomerServlet?msg=Customer updated");

        } catch (NumberFormatException nfe) {
            response.sendRedirect("ViewCustomerServlet?msg=Account No and Units must be numbers");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewCustomerServlet?msg=Failed to update customer");
        }
    }
}
