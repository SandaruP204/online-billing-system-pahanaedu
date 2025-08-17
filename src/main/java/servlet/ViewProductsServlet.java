package servlet;

import dao.ProductDAO;
import dao.impl.ProductDAOimpl;
import model.Product;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/viewProducts")
public class ViewProductsServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() {
        // Program to the interface; use your impl behind it
        this.productDAO = new ProductDAOimpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Product> productList = productDAO.getAllProducts();
            request.setAttribute("products", productList);

            RequestDispatcher dispatcher = request.getRequestDispatcher("manage-products.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?msg=Failed to load products");
        }
    }
}
