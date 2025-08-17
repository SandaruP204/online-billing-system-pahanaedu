package servlet;

import dao.CustomerDAO;
import dao.impl.CustomerDAOimpl;
import model.Customer;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/EditCustomerServlet")
public class EditCustomerServlet extends HttpServlet {

    private CustomerDAO customerDAO;

    @Override
    public void init() {
        this.customerDAO = new CustomerDAOimpl(); // program to the interface
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

            Customer customer = customerDAO.getCustomer(accountNo);
            if (customer == null) {
                response.sendRedirect("ViewCustomerServlet?msg=Customer not found");
                return;
            }

            request.setAttribute("customer", customer);
            RequestDispatcher dispatcher = request.getRequestDispatcher("customer.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect("ViewCustomerServlet?msg=Account number must be a number");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewCustomerServlet?msg=Failed to load customer");
        }
    }
}
