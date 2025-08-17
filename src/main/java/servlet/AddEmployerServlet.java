package servlet;

import dao.UserDAO;
import dao.impl.UserDAOimpl;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/addEmployer")
public class AddEmployerServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        this.userDAO = new UserDAOimpl(); // program to interface, use impl
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String role = "employer"; // fixed role

            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                response.sendRedirect("add-employer.jsp?msg=Username and password are required.");
                return;
            }

            // prevent duplicates
            if (userDAO.usernameExists(username)) {
                response.sendRedirect("add-employer.jsp?msg=Username already exists.");
                return;
            }

            // Build user (password stored as-is; match your DAO/DB scheme)
            User u = new User();
            u.setUsername(username.trim());
            u.setPassword(password); // If you later hash, do it before setPassword
            u.setRole(role);

            userDAO.create(u);

            response.sendRedirect("add-employer.jsp?msg=Employer added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("add-employer.jsp?msg=Error occurred.");
        }
    }
}
