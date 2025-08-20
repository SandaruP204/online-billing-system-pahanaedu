<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%
  String username = (String) session.getAttribute("username");
  String role = (String) session.getAttribute("role");
  if (username == null) {
    response.sendRedirect("Login.jsp");
    return;
  }
  // Guard to employer role (normalize case)
  String norm = role == null ? "" : role.trim().toUpperCase();
  if (!"EMPLOYER".equals(norm)) {
    response.sendRedirect("index.jsp"); // fallback to admin dashboard
    return;
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Employer Dashboard</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="css/employer-dashboard.css">
</head>
<body>
<div class="wrap">
  <header class="topbar">
    <div class="brand">Pahana Employer</div>
    <div class="user">
      <span class="hello">Hi, <strong><%= username %></strong> <span class="badge">EMPLOYER</span></span>
      <a class="btn ghost" href="Login.jsp">Logout</a>
      <%-- Prefer a LogoutServlet that invalidates session: href="logout" --%>
    </div>
  </header>

  <section class="hero">
    <h1>Employer Dashboard</h1>
    <p class="muted">Create bills and manage customers quickly.</p>
  </section>

  <section class="grid">
    <!-- Create Bill -->
    <a class="card link" href="addBill">
      <div class="icon">🧾</div>
      <h3>Create Bill</h3>
      <p>Build a new bill from existing customers and products.</p>
      <span class="cta">Create →</span>
    </a>

    <!-- View Bills -->
    <a class="card link" href="viewBills">
      <div class="icon">📑</div>
      <h3>View Bills</h3>
      <p>Browse, open, and print previous bills.</p>
      <span class="cta">Open →</span>
    </a>

    <!-- Manage Customers (combined add + list page you built) -->
    <a class="card link" href="ViewCustomerServlet">
      <div class="icon">👥</div>
      <h3>Manage Customers</h3>
      <p>Add new customers or edit existing ones.</p>
      <span class="cta">Manage →</span>
    </a>
  </section>

  <a class="card link" href="help.jsp">
    <div class="icon">❓</div>
    <h3>Help & Support</h3>
    <p>Find guidance on using the system.</p>
    <span class="cta">Open →</span>
  </a>

  <section class="quick-actions">
    <h2>Quick Actions</h2>
    <div class="qa-grid">
      <a class="btn small primary" href="addBill">+ New Bill</a>
      <a class="btn small ghost" href="ViewCustomerServlet">+ New Customer</a>
      <a class="btn small ghost" href="viewBills">Recent Bills</a>
    </div>
  </section>

  <footer class="foot">
    <span class="muted">© <script>document.write(new Date().getFullYear())</script> Pahana Utilities</span>
  </footer>
</div>
</body>
</html>
