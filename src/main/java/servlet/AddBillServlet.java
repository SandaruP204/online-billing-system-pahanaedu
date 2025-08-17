package servlet;

import dao.BillDAO;
import dao.CustomerDAO;
import dao.ProductDAO;

import dao.impl.BillDAOImpl;
import dao.impl.CustomerDAOimpl;
import dao.impl.ProductDAOimpl;

import model.Bill;
import model.BillItem;
import model.Customer;
import model.Product;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/addBill")
public class AddBillServlet extends HttpServlet {

    private BillDAO billDAO;
    private CustomerDAO customerDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        // Program to interfaces; use impls underneath
        this.billDAO     = new BillDAOImpl();
        this.customerDAO = new CustomerDAOimpl();
        this.productDAO  = new ProductDAOimpl();
    }

    // ------- GET: show form with preloaded suggestions -------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            List<Product>  products  = productDAO.getAllProducts();

            request.setAttribute("customers", customers);
            request.setAttribute("products",  products);

            // forward (not redirect) so attributes are available
            RequestDispatcher rd = request.getRequestDispatcher("addBill.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?msg=Failed to load Add Bill form");
        }
    }

    // ------- POST: create bill (your validated logic kept) -------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Parse customer/account
            String accountNoStr = request.getParameter("accountNo");
            if (accountNoStr == null || accountNoStr.isBlank()) {
                throw new IllegalArgumentException("Account number is required.");
            }
            int accountNo = Integer.parseInt(accountNoStr);

            // Parse arrays (may include blanks if the form has extra rows)
            String[] productNos = request.getParameterValues("productNo");
            String[] quantities = request.getParameterValues("quantity");

            List<BillItem> items = new ArrayList<>();

            if (productNos != null && quantities != null) {
                int len = Math.min(productNos.length, quantities.length);
                for (int i = 0; i < len; i++) {
                    String pStr = productNos[i];
                    String qStr = quantities[i];

                    // skip empty lines
                    if (pStr == null || pStr.isBlank() || qStr == null || qStr.isBlank()) continue;

                    int productNo = Integer.parseInt(pStr);
                    int quantity  = Integer.parseInt(qStr);

                    if (productNo <= 0) {
                        throw new IllegalArgumentException("Invalid product number at row " + (i + 1));
                    }
                    if (quantity <= 0) {
                        throw new IllegalArgumentException("Quantity must be > 0 at row " + (i + 1));
                    }

                    // Price/total computed in DAO from DB prices
                    items.add(new BillItem(0, 0, productNo, quantity));
                }
            }

            if (items.isEmpty()) {
                throw new IllegalArgumentException("Please add at least one product to the bill.");
            }

            // Build Bill
            Bill bill = new Bill();
            bill.setAccountNo(accountNo);
            bill.setBillDate(new Date());
            bill.setItems(items);

            int billId = billDAO.addBill(bill);

            if (billId != -1) {
                response.sendRedirect("success.jsp?msg=Bill created successfully. Bill ID=" + billId);
            } else {
                response.setContentType("text/plain");
                response.getWriter().write("Failed to create bill for unknown reasons.");
            }

        } catch (NumberFormatException nfe) {
            response.setContentType("text/plain");
            response.getWriter().write("Invalid number in form: " + nfe.getMessage());
        } catch (IllegalArgumentException iae) {
            // Re-show form with error + preloaded lists so user can fix quickly
            try {
                request.setAttribute("error", iae.getMessage());
                request.setAttribute("customers", customerDAO.getAllCustomers());
                request.setAttribute("products",  productDAO.getAllProducts());
                request.getRequestDispatcher("addBill.jsp").forward(request, response);
            } catch (Exception fwdEx) {
                response.setContentType("text/plain");
                response.getWriter().write("Error: " + iae.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("Error occurred while creating bill: " + e.getMessage());
        }
    }
}
