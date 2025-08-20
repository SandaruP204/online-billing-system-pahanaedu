package servlet;

import dao.CustomerDAO;
import dao.ProductDAO;
import dao.impl.CustomerDAOimpl;
import dao.impl.ProductDAOimpl;

import model.Customer;
import model.Product;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import service.BillingService;
import service.impl.BillingServiceImpl;

@WebServlet("/addBill")
public class AddBillServlet extends HttpServlet {

    private CustomerDAO customerDAO;
    private ProductDAO  productDAO;
    private BillingService billing; // <-- business layer

    @Override
    public void init() throws ServletException {
        this.customerDAO = new CustomerDAOimpl();
        this.productDAO  = new ProductDAOimpl();
        this.billing     = new BillingServiceImpl(); // <-- use the service layer
    }

    // ------- GET: show form with preloaded suggestions -------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            request.setAttribute("customers", customerDAO.getAllCustomers());
            request.setAttribute("products",  productDAO.getAllProducts());
            request.getRequestDispatcher("addBill.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?msg=Failed to load Add Bill form");
        }
    }

    // ------- POST: create bill via BillingService (atomic, no negative stock) -------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String ctx = request.getContextPath();

        try {
            // Parse customer/account
            String accountNoStr = request.getParameter("accountNo");
            if (accountNoStr == null || accountNoStr.isBlank()) {
                throw new IllegalArgumentException("Account number is required.");
            }
            int accountNo = Integer.parseInt(accountNoStr);

            // Parse arrays (skip blank rows)
            String[] productNos = request.getParameterValues("productNo");
            String[] quantities = request.getParameterValues("quantity");
            if (productNos == null || quantities == null || productNos.length == 0) {
                throw new IllegalArgumentException("Please add at least one product to the bill.");
            }
            if (productNos.length != quantities.length) {
                throw new IllegalArgumentException("Product and quantity rows do not match.");
            }

            List<BillingService.Line> lines = new ArrayList<>();
            for (int i = 0; i < productNos.length; i++) {
                String pStr = productNos[i];
                String qStr = quantities[i];
                if (pStr == null || pStr.isBlank() || qStr == null || qStr.isBlank()) continue; // ignore empty row

                int productNo = Integer.parseInt(pStr);
                int qty       = Integer.parseInt(qStr);

                if (productNo <= 0) throw new IllegalArgumentException("Invalid product number at row " + (i + 1));
                if (qty <= 0)       throw new IllegalArgumentException("Quantity must be > 0 at row " + (i + 1));

                lines.add(new BillingService.Line(productNo, qty));
            }
            if (lines.isEmpty()) {
                throw new IllegalArgumentException("Please add at least one product to the bill.");
            }

            // Create bill (service manages transaction + stock checks)
            int billId = billing.createBill(accountNo, lines);

            // success → flash + redirect (adjust destination if your mapping differs)
            request.getSession(true).setAttribute("flash_success", "Bill created successfully (ID #" + billId + ").");
            response.sendRedirect(ctx + "/viewBill?billId=" + billId); // or ctx + "/ViewBillsServlet" / your page
            return;

        } catch (NumberFormatException nfe) {
            request.setAttribute("error", "Please enter valid numbers.");
            reloadFormAndForward(request, response);
        } catch (BillingService.InsufficientStockException ise) {
            // Friendly message; nothing was inserted/updated (rolled back)
            request.setAttribute("error", ise.getMessage());
            reloadFormAndForward(request, response);
        } catch (IllegalArgumentException iae) {
            request.setAttribute("error", iae.getMessage());
            reloadFormAndForward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // You can send to a generic error page or use flash
            request.getSession(true).setAttribute("flash_error", "Failed to create bill. Please try again.");
            response.sendRedirect(ctx + "/addBill");
        }
    }

    private void reloadFormAndForward(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("customers", customerDAO.getAllCustomers());
            request.setAttribute("products",  productDAO.getAllProducts());
        } catch (Exception ignore) {}
        request.getRequestDispatcher("addBill.jsp").forward(request, response);
    }
}
