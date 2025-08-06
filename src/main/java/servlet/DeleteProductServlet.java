package servlet;

import dao.ProductDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/DeleteProductServlet")
public class DeleteProductServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productNo = Integer.parseInt(request.getParameter("productNo"));
        ProductDAO dao = new ProductDAO();
        dao.deleteProduct(productNo);
        response.sendRedirect("viewProducts");
    }
}
