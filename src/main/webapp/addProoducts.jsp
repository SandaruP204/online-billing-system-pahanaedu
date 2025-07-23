<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 23/07/2025
  Time: 14:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Your Products!</title>
</head>
<body>
<h2>Add New Products!</h2>
<form action="AddCustomerServlet" method="post">
    <label>Account Number:</label><input type="number" name="accountNo" required><br>
    <label>Name:</label><input type="text" name="name" required><br>
    <label>Address:</label><input type="text" name="address" required><br>
    <label>Phone:</label><input type="text" name="phone" required><br>
    <label>Units Consumed:</label><input type="number" name="unitsConsumed" required><br>
    <button type="submit">Add Customer</button>
</form>
</body>
</html>
