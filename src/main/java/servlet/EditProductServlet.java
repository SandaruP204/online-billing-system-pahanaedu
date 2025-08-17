package servlet;

import dao.ProductDAO;
import dao.impl.ProductDAOimpl;
import model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/EditProductServlet")
public class EditProductServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() {
        this.productDAO = new ProductDAOimpl(); // interface + impl
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String productNoStr = request.getParameter("productNo");
            String name         = request.getParameter("name");
            String unitStr      = request.getParameter("unit");
            String priceStr     = request.getParameter("price");

            if (productNoStr == null || productNoStr.isBlank()
                    || name == null || name.isBlank()
                    || unitStr == null || unitStr.isBlank()
                    || priceStr == null || priceStr.isBlank()) {
                response.sendRedirect("viewProducts?msg=All fields are required");
                return;
            }

            int productNo = Integer.parseInt(productNoStr);
            int unit      = Integer.parseInt(unitStr);
            double price  = Double.parseDouble(priceStr);

            if (productNo <= 0) { response.sendRedirect("viewProducts?msg=Invalid product number"); return; }
            if (unit < 0)       { response.sendRedirect("viewProducts?msg=Unit cannot be negative"); return; }
            if (price < 0)      { response.sendRedirect("viewProducts?msg=Price cannot be negative"); return; }

            Product p = new Product();
            p.setProductNo(productNo);
            p.setName(name.trim());
            p.setUnit(unit);
            p.setPrice(price);

            // ✅ call the method your impl actually has
            productDAO.updateProduct(p);

            response.sendRedirect("viewProducts?msg=Product updated");

        } catch (NumberFormatException nfe) {
            response.sendRedirect("viewProducts?msg=Invalid number format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("viewProducts?msg=Error updating product");
        }
    }
}
