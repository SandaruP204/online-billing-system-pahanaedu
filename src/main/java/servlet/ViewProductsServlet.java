package servlet;

import dao.ProductDAO;
import model.Product;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.annotation.WebServlet;


@WebServlet("/viewProducts")
public class ViewProductsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductDAO dao = new ProductDAO();
        List<Product> productList = dao.getAllProducts();
        request.setAttribute("products", productList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("viewProducts.jsp");
        dispatcher.forward(request, response);
    }
}
