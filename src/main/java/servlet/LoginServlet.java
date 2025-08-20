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
        this.userDAO = new UserDAOimpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String ctx = request.getContextPath();
        // If already logged in, don't show the login page again.
        HttpSession s = request.getSession(false);
        boolean loggedIn = s != null && (s.getAttribute("authUser") != null || s.getAttribute("username") != null);
        if (loggedIn) {
            String intended = (String) s.getAttribute("intended");
            s.removeAttribute("intended");
            String dest = (isSafeRelativePath(intended)) ? intended : "/index.jsp";
            response.sendRedirect(ctx + dest);
            return;
        }

        // Otherwise, render the login page
        request.getRequestDispatcher("Login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String ctx = request.getContextPath();

        try {
            String username = trimOrEmpty(request.getParameter("username"));
            String password = trimOrEmpty(request.getParameter("password"));

            if (username.isEmpty() || password.isEmpty()) {
                request.getSession(true).setAttribute("flash_error", "Username and password are required.");
                response.sendRedirect(ctx + "/Login.jsp");
                return;
            }

            boolean ok = userDAO.validateLogin(username, password);
            if (!ok) {
                request.getSession(true).setAttribute("flash_error", "Invalid username or password.");
                response.sendRedirect(ctx + "/Login.jsp");
                return;
            }

            User user = userDAO.findByUsername(username);
            if (user == null) {
                request.getSession(true).setAttribute("flash_error", "User record not found.");
                response.sendRedirect(ctx + "/Login.jsp");
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("authUser", user);               // key the AuthFilter checks
            session.setAttribute("username", user.getUsername()); // optional
            session.setAttribute("role", user.getRole());         // optional
            session.setMaxInactiveInterval(30 * 60);              // 30 mins

            session.setAttribute("flash_success", "Welcome, " + user.getUsername() + "! Logged in successfully.");

            String intended = (String) session.getAttribute("intended");
            session.removeAttribute("intended");

            String normRole = (user.getRole() == null ? "" : user.getRole().trim().toUpperCase());
            boolean isEmployer = "EMPLOYER".equals(normRole) || "CASHIER".equals(normRole);

            String fallback = isEmployer ? "/employer-dashboard.jsp" : "/index.jsp";
            String dest = (isSafeRelativePath(intended)) ? intended : fallback;

            response.sendRedirect(ctx + dest);

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession(true).setAttribute("flash_error", "Something went wrong. Please try again.");
            response.sendRedirect(ctx + "/Login.jsp");
        }
    }

    // ---------- helpers ----------
    private static String trimOrEmpty(String s) {
        return (s == null) ? "" : s.trim();
    }

    private static boolean isSafeRelativePath(String p) {
        if (p == null || p.isBlank()) return false;
        if (!p.startsWith("/")) return false;
        if (p.startsWith("//")) return false;
        if (p.contains("\r") || p.contains("\n")) return false;
        return true;
    }
}
