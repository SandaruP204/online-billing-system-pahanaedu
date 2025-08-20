<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("Login.jsp");
        return;
    }
    // Optional: role-based tweaks
    String role = (String) session.getAttribute("role");
    String displayName = username; // you can format if needed
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/dashboardstyle.css">
</head>
<body>
<div class="wrap">
    <header class="topbar">
        <div class="brand">Pahana Admin</div>
        <div class="user">
            <span class="hello">Hi, <strong><%= displayName %></strong></span>
            <a class="btn ghost" href="logout">Logout</a>
            <%-- If you have a LogoutServlet that invalidates the session, use it:
                 <a class="btn ghost" href="logout">Logout</a>
            --%>
        </div>
        <%@ include file="/flash.jspf" %>
    </header>

    <section class="hero">
        <h1>Dashboard</h1>
        <p class="muted">
            Quick actions for products, customers, and billing. Choose a module to get started.
        </p>
    </section>

    <section class="grid">
        <a class="card link" href="viewProducts">
            <div class="icon">📦</div>
            <h3>Manage Products</h3>
            <p>Add, edit, or delete products and stock.</p>
            <span class="cta">Open →</span>
        </a>

        <a class="card link" href="ViewCustomerServlet">
            <div class="icon">👥</div>
            <h3>Manage Customers</h3>
            <p>Create and update customer records.</p>
            <span class="cta">Open →</span>
        </a>

        <a class="card link" href="addBill">
            <div class="icon">🧾</div>
            <h3>Create Bill</h3>
            <p>Build a new bill from existing customers and products.</p>
            <span class="cta">Create →</span>
        </a>

        <a class="card link" href="viewBills">
            <div class="icon">📑</div>
            <h3>View Bills</h3>
            <p>Browse and inspect previous bills.</p>
            <span class="cta">View →</span>
        </a>

        <a class="card link" href="add-employer.jsp">
            <div class="icon">🧑‍💼</div>
            <h3>Manage Employers</h3>
            <p>Add new employer accounts.</p>
            <span class="cta">Open →</span>
        </a>

        <a class="card link" href="help.jsp">
            <div class="icon">❓</div>
            <h3>Help & Support</h3>
            <p>Find guidance on using the system.</p>
            <span class="cta">Open →</span>
        </a>

        <a class="card link" href="reports/monthly">
            <div class="icon">📊</div>
            <h3>Monthly Report</h3>
            <p>Revenue, bill counts, daily totals.</p>
            <span class="cta">Open →</span>
        </a>


    </section>

    <footer class="foot">
        <span class="muted">© <script>document.write(new Date().getFullYear())</script> Pahana Utilities</span>
    </footer>
</div>
</body>
</html>
