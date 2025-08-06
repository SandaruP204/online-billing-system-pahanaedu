package servlet;

import dao.ProductDAO;
import model.Product;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/EditProductServlet")
public class EditProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int productNo = Integer.parseInt(request.getParameter("productNo"));
        String name = request.getParameter("name");
        int unit = Integer.parseInt(request.getParameter("unit"));
        double price = Double.parseDouble(request.getParameter("price"));

        Product product = new Product();
        product.setProductNo(productNo);
        product.setName(name);
        product.setUnit(unit);
        product.setPrice(price);

        ProductDAO dao = new ProductDAO();
        dao.updateProduct(product); // Make sure this exists!

        response.sendRedirect("ViewProductServlet");
    }
}
