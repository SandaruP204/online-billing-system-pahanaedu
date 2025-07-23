package servlet;

import java.io.IOException;

import model.Product;
import dao.ProductDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/AddProductServlet")
public class AddProductServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int productNo = Integer.parseInt(request.getParameter("productNo"));
        String name = request.getParameter("name");
        int unit = Integer.parseInt(request.getParameter("unit"));

        Product product = new Product();
        product.setProductNo(productNo);
        product.setName(name);
        product.setUnit(unit);

        ProductDAO dao = new ProductDAO();
        dao.addProduct(product);

        response.sendRedirect("success.jsp");
    }
}
