package servlet;

import dao.CustomerDAO;
import dao.impl.CustomerDAOimpl;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/DeleteCustomerServlet")
public class DeleteCustomerServlet extends HttpServlet {

    private CustomerDAO customerDAO;

    @Override
    public void init() {
        this.customerDAO = new CustomerDAOimpl(); // program to the interface
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String accountNoStr = request.getParameter("accountNo");
            if (accountNoStr == null || accountNoStr.isBlank()) {
                response.sendRedirect("ViewCustomerServlet?msg=Account number is required");
                return;
            }

            int accountNo = Integer.parseInt(accountNoStr);
            if (accountNo <= 0) {
                response.sendRedirect("ViewCustomerServlet?msg=Invalid account number");
                return;
            }

            customerDAO.deleteCustomer(accountNo);
            response.sendRedirect("ViewCustomerServlet?msg=Customer deleted");

        } catch (NumberFormatException nfe) {
            response.sendRedirect("ViewCustomerServlet?msg=Account number must be a number");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewCustomerServlet?msg=Failed to delete customer");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response); // reuse logic
    }
}
