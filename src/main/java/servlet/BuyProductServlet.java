package servlet;

import dao.ProductDAO;
import dao.impl.ProductDAOimpl;
import model.Product;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/buyProduct")
public class BuyProductServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        // Program to the interface; use impl under the hood
        this.productDAO = new ProductDAOimpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Product> products = productDAO.getAllProducts();
            request.setAttribute("products", products);
            request.getRequestDispatcher("buyProduct.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?msg=Failed to load products.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String productNoStr = request.getParameter("productNo");
            String qtyStr = request.getParameter("quantity");

            if (productNoStr == null || productNoStr.isBlank() ||
                    qtyStr == null || qtyStr.isBlank()) {
                throw new IllegalArgumentException("Please select a product and quantity.");
            }

            int productNo = Integer.parseInt(productNoStr);
            int quantity = Integer.parseInt(qtyStr);

            if (productNo <= 0) throw new IllegalArgumentException("Invalid product number.");
            if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than 0.");

            // Load product for price & name
            Product product = productDAO.findById(productNo);
            if (product == null) {
                request.setAttribute("error", "Product not found.");
                request.getRequestDispatcher("buyProduct.jsp").forward(request, response);
                return;
            }

            // Atomic decrement to avoid race conditions
            boolean ok = productDAO.decrementStock(productNo, quantity);
            if (!ok) {
                request.setAttribute("error", "Not enough stock for " + product.getName() + ".");
                request.getRequestDispatcher("buyProduct.jsp").forward(request, response);
                return;
            }

            double total = product.getPrice() * quantity;

            // Pass details to bill page
            request.setAttribute("product", product);
            request.setAttribute("quantity", quantity);
            request.setAttribute("total", total);

            request.getRequestDispatcher("bill.jsp").forward(request, response);

        } catch (NumberFormatException nfe) {
            request.setAttribute("error", "Please enter valid numbers for product and quantity.");
            request.getRequestDispatcher("buyProduct.jsp").forward(request, response);
        } catch (IllegalArgumentException iae) {
            request.setAttribute("error", iae.getMessage());
            request.getRequestDispatcher("buyProduct.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?msg=Failed to process purchase.");
        }
    }
}
