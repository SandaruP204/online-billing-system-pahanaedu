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
<form action="AddProductServlet" method="post">
    <label>Product Number:</label><input type="number" name="productNo" required><br>
    <label>Name:</label><input type="text" name="name" required><br>
    <label>Quantity:</label><input type="number" name="unit" required><br>
    <button type="submit">Add Product</button>
</form>
</body>
</html>
