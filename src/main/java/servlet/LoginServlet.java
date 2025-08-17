package servlet;

import dao.UserDAO;
import dao.impl.UserDAOimpl;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        // Program to the interface; impl handles its own DB connections
        this.userDAO = new UserDAOimpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                request.setAttribute("error", "Username and password are required.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
                return;
            }

            // Hash + verify (inside DAO)
            boolean ok = userDAO.validateLogin(username, password);
            if (!ok) {
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
                return;
            }

            // Load full user (id, role, etc.)
            User user = userDAO.findByUsername(username);
            if (user == null) {
                request.setAttribute("error", "User record not found.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            // Normalize role handling (supports ADMIN/admin/Employer/etc.)
            String role = user.getRole();
            String norm = role == null ? "" : role.trim().toUpperCase();

            if ("ADMIN".equals(norm)) {
                response.sendRedirect("index.jsp");
            } else if ("EMPLOYER".equals(norm)) {
                // TODO: set your employer landing
                response.sendRedirect("employer-dashboard.jsp");
            } else {
                // Fallback if an unexpected role sneaks in
                response.sendRedirect("index.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Something went wrong. Please try again.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }
}
