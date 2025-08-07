<%@ page language="java" %>
<html>
<head>
    <title>Add New Customer</title>
    <link rel="stylesheet" href="css/acstyle.css">
</head>
<body>
<h2>Add New Customer</h2>
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
