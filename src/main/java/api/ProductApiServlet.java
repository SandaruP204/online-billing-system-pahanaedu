package api;

import dao.ProductDAO;
import model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/products")
public class ProductApiServlet extends HttpServlet {

    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Product> products = productDAO.getAllProducts();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            json.append("{")
                    .append("\"productNo\":").append(p.getProductNo()).append(",")
                    .append("\"name\":\"").append(p.getName()).append("\",")
                    .append("\"unit\":").append(p.getUnit()).append(",")
                    .append("\"price\":").append(p.getPrice())
                    .append("}");
            if (i < products.size() - 1) json.append(",");
        }
        json.append("]");

        response.getWriter().write(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productNo = Integer.parseInt(request.getParameter("productNo"));
        String name = request.getParameter("name");
        int unit = Integer.parseInt(request.getParameter("unit"));
        double price = Double.parseDouble(request.getParameter("price"));

        Product product = new Product();
        product.setProductNo(productNo);
        product.setName(name);
        product.setUnit(unit);
        product.setPrice(price);

        productDAO.addProduct(product);

        response.setContentType("application/json");
        response.getWriter().write("{\"success\":true}");
    }
}
