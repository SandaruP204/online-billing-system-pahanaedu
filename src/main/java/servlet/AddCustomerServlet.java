package servlet;

import dao.CustomerDAO;
import dao.impl.CustomerDAOimpl;
import model.Customer;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AddCustomerServlet")
public class AddCustomerServlet extends HttpServlet {

    private CustomerDAO customerDAO;

    @Override
    public void init() throws ServletException {
        // Program to the interface, instantiate the impl
        this.customerDAO = new CustomerDAOimpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Parse + basic checks
            String accountNoStr = request.getParameter("accountNo");
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String unitsStr = request.getParameter("unitsConsumed");

            if (accountNoStr == null || accountNoStr.isBlank()
                    || name == null || name.isBlank()
                    || address == null || address.isBlank()
                    || phone == null || phone.isBlank()
                    || unitsStr == null || unitsStr.isBlank()) {
                throw new IllegalArgumentException("All fields are required.");
            }

            int accountNo = Integer.parseInt(accountNoStr);
            int units = Integer.parseInt(unitsStr);
            if (accountNo <= 0) throw new IllegalArgumentException("Account number must be positive.");
            if (units < 0) throw new IllegalArgumentException("Units consumed cannot be negative.");

            Customer customer = new Customer();
            customer.setAccountNo(accountNo);
            customer.setName(name.trim());
            customer.setAddress(address.trim());
            customer.setPhone(phone.trim());
            customer.setUnitsConsumed(units);

            customerDAO.addCustomer(customer);

            response.sendRedirect("success.jsp?msg=Customer added");

        } catch (NumberFormatException nfe) {
            request.setAttribute("error", "Account No and Units must be numbers.");
            request.getRequestDispatcher("/manage-customers.jsp").forward(request, response);
        } catch (IllegalArgumentException iae) {
            request.setAttribute("error", iae.getMessage());
            request.getRequestDispatcher("/manage-customers.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?msg=Failed to add customer");
        }
    }
}
