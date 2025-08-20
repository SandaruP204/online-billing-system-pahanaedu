package Auth;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Set;

@WebFilter(
        urlPatterns = "/*",
        dispatcherTypes = {
                DispatcherType.REQUEST,
                DispatcherType.FORWARD,
                DispatcherType.INCLUDE,
                DispatcherType.ERROR
        }
)
public class AuthFilter implements Filter {

    private static final Set<String> PUBLIC = Set.of(
            "/login", "/Login", "/Login.jsp",
            "/css/", "/js/", "/images/", "/assets/", "/favicon", "/webjars/"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String ctx  = req.getContextPath();                       // e.g. /onlinebill
        String path = req.getRequestURI().substring(ctx.length()); // e.g. /index.jsp

        // TEMP DEBUG (uncomment if needed)
        // System.out.println("[AuthFilter] path=" + path);

        if (isPublic(path) || isStatic(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean loggedIn =
                session != null &&
                        (session.getAttribute("authUser") != null || session.getAttribute("username") != null);

        if (!loggedIn) {
            req.getSession(true).setAttribute("intended", path);

            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            res.setHeader("Pragma", "no-cache");
            res.setDateHeader("Expires", 0);

            res.sendRedirect(ctx + "/Login.jsp");
            return;
        }

        // no-cache for protected pages
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        if (path.equals("/") || path.equalsIgnoreCase("/login") || path.equalsIgnoreCase("/Login.jsp")) return true;
        for (String p : PUBLIC) if (path.startsWith(p)) return true;
        return false;
    }
    private boolean isStatic(String path) {
        return path.endsWith(".css") || path.endsWith(".js") ||
                path.endsWith(".png") || path.endsWith(".jpg") ||
                path.endsWith(".jpeg")|| path.endsWith(".gif") ||
                path.endsWith(".ico") || path.endsWith(".svg") ||
                path.endsWith(".woff")|| path.endsWith(".woff2")||
                path.endsWith(".ttf");
    }
}
