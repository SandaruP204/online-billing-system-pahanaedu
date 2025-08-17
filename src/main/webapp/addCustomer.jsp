<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add New Customer</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/add-customer.css">
</head>
<body>
<div class="wrap">
    <div class="card">
        <div class="header">
            <h2>Add New Customer</h2>
        </div>

        <form action="AddCustomerServlet" method="post" class="form">
            <div class="field">
                <label for="accountNo">Account Number</label>
                <input id="accountNo" type="number" name="accountNo" required placeholder="Enter account number">
            </div>

            <div class="field">
                <label for="name">Name</label>
                <input id="name" type="text" name="name" required placeholder="Enter customer name">
            </div>

            <div class="field">
                <label for="address">Address</label>
                <input id="address" type="text" name="address" required placeholder="Enter address">
            </div>

            <div class="field">
                <label for="phone">Phone</label>
                <input id="phone" type="text" name="phone" required placeholder="Enter phone number">
            </div>

            <div class="field">
                <label for="unitsConsumed">Units Consumed</label>
                <input id="unitsConsumed" type="number" name="unitsConsumed" required placeholder="Enter units consumed">
            </div>

            <div class="actions">
                <button type="submit" class="btn primary">Add Customer</button>
                <a href="viewCustomers.jsp" class="btn ghost">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
