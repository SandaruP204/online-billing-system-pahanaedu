package servlet;

import dao.ProductDAO;
import model.Product;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/buyProduct")
public class BuyProductServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDAO dao = new ProductDAO();
        List<Product> products = dao.getAllProducts();

        request.setAttribute("products", products);
        RequestDispatcher dispatcher = request.getRequestDispatcher("buyProduct.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int productNo = Integer.parseInt(request.getParameter("productNo"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        ProductDAO dao = new ProductDAO();
        Product product = dao.getProductByNo(productNo);

        if (product != null && quantity <= product.getUnit()) {
            double total = product.getPrice() * quantity;

            int newStock = product.getUnit() - quantity;
            dao.updateProductStock(productNo, newStock);

            request.setAttribute("total", total);
            RequestDispatcher dispatcher = request.getRequestDispatcher("bill.jsp");
            dispatcher.forward(request, response);
        } else {
            request.setAttribute("error", "Not enough stock or product not found!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("buyProduct.jsp");
            dispatcher.forward(request, response);
        }
    }
}
