package servlet;

import java.io.IOException;

import dao.ProductDAO;
import dao.impl.ProductDAOimpl;
import model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AddProductServlet")
public class AddProductServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        // program to the interface
        this.productDAO = new ProductDAOimpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String productNoStr = request.getParameter("productNo");
            String name = request.getParameter("name");
            String unitStr = request.getParameter("unit");
            String priceStr = request.getParameter("price");

            if (productNoStr == null || productNoStr.isBlank()
                    || name == null || name.isBlank()
                    || unitStr == null || unitStr.isBlank()
                    || priceStr == null || priceStr.isBlank()) {
                throw new IllegalArgumentException("All fields are required.");
            }

            int productNo = Integer.parseInt(productNoStr);
            int unit = Integer.parseInt(unitStr);
            double price = Double.parseDouble(priceStr);

            if (productNo <= 0) throw new IllegalArgumentException("Product number must be positive.");
            if (unit < 0) throw new IllegalArgumentException("Unit cannot be negative.");
            if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");

            Product product = new Product();
            product.setProductNo(productNo);
            product.setName(name.trim());
            product.setUnit(unit);
            product.setPrice(price);

            productDAO.addProduct(product);

            response.sendRedirect("success.jsp?msg=Product added");

        } catch (NumberFormatException nfe) {
            request.setAttribute("error", "Product No, Unit and Price must be valid numbers.");
            request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
        } catch (IllegalArgumentException iae) {
            request.setAttribute("error", iae.getMessage());
            request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?msg=Failed to add product.");
        }
    }
}
