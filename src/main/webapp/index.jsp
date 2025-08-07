<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>
<div class="dashboard">
    <h1>Welcome, <%= username %>!</h1>
    <a href="addProducts.jsp"> Add Products</a>
    <a href="viewProducts"> Manage Products</a>
    <a href="buyProduct"> Buy Products</a>
    <a href="addCustomer.jsp"> Add Customers</a>
    <a href="ViewCustomerServlet"> Manage Customers</a>
    <a href="add-employer.jsp"> Manage Employers</a>

    <div class="logout">
        <p><a href="Login.jsp">Logout</a></p>
    </div>
</div>
</body>
</html>
