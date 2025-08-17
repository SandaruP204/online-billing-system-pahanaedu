package api;

import dao.ProductDAO;
import dao.impl.ProductDAOimpl;
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

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        this.productDAO = new ProductDAOimpl(); // program to interface
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try {
            List<Product> products = productDAO.getAllProducts();
            StringBuilder json = new StringBuilder();
            json.append("[");

            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                json.append("{")
                        .append("\"productNo\":").append(p.getProductNo()).append(",")
                        .append("\"name\":\"").append(escapeJson(p.getName())).append("\",")
                        .append("\"unit\":").append(p.getUnit()).append(",")
                        .append("\"price\":").append(p.getPrice())
                        .append("}");
                if (i < products.size() - 1) json.append(",");
            }

            json.append("]");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to load products\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try {
            String productNoStr = request.getParameter("productNo");
            String name         = request.getParameter("name");
            String unitStr      = request.getParameter("unit");
            String priceStr     = request.getParameter("price");

            if (isBlank(productNoStr) || isBlank(name) || isBlank(unitStr) || isBlank(priceStr)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"All fields are required\"}");
                return;
            }

            int productNo = Integer.parseInt(productNoStr);
            int unit      = Integer.parseInt(unitStr);
            double price  = Double.parseDouble(priceStr);

            if (productNo <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Product number must be positive\"}");
                return;
            }
            if (unit < 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Unit cannot be negative\"}");
                return;
            }
            if (price < 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Price cannot be negative\"}");
                return;
            }

            Product product = new Product();
            product.setProductNo(productNo);
            product.setName(name.trim());
            product.setUnit(unit);
            product.setPrice(price);

            productDAO.addProduct(product);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"success\":true}");

        } catch (NumberFormatException nfe) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid number format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to create product\"}");
        }
    }

    // ---------- helpers ----------

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Minimal JSON string escaper for names etc.
    private static String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder out = new StringBuilder(s.length() + 16);
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"'  -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\b' -> out.append("\\b");
                case '\f' -> out.append("\\f");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> {
                    if (c < 0x20) out.append(String.format("\\u%04x", (int)c));
                    else out.append(c);
                }
            }
        }
        return out.toString();
    }
}
