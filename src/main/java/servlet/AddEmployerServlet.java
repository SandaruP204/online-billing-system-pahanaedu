package servlet;

import dao.DBConnection;  // Your existing DB connection helper

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/addEmployer")
public class AddEmployerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = "employer";  // fixed role for employers

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); // **Important**: Hash passwords for production!
            stmt.setString(3, role);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                response.sendRedirect("add-employer.jsp?msg=Employer added successfully!");
            } else {
                response.sendRedirect("add-employer.jsp?msg=Failed to add employer.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("add-employer.jsp?msg=Error occurred.");
        }
    }
}
