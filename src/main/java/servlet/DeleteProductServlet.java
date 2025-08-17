package servlet;

import dao.ProductDAO;
import dao.impl.ProductDAOimpl;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/DeleteProductServlet")
public class DeleteProductServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() {
        this.productDAO = new ProductDAOimpl(); // program to the interface
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String productNoStr = request.getParameter("productNo");
            if (productNoStr == null || productNoStr.isBlank()) {
                response.sendRedirect("viewProducts?msg=Product number is required");
                return;
            }

            int productNo = Integer.parseInt(productNoStr);
            if (productNo <= 0) {
                response.sendRedirect("viewProducts?msg=Invalid product number");
                return;
            }

            productDAO.deleteProduct(productNo);
            response.sendRedirect("viewProducts?msg=Product deleted");

        } catch (NumberFormatException nfe) {
            response.sendRedirect("viewProducts?msg=Product number must be a number");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("viewProducts?msg=Failed to delete product");
        }
    }
}
