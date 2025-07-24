<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Customer" %>
<%@ page import="java.util.List" %>

<html>
<head><title>All Customers</title></head>
<body>
<h1>All Customers</h1>

<table border="1">
    <tr>
        <th>Account No</th>
        <th>Name</th>
        <th>Address</th>
        <th>Phone</th>
        <th>Units Consumed</th>
    </tr>

    <%
        List<Customer> customers = (List<Customer>) request.getAttribute("customers");
        if (customers != null && !customers.isEmpty()) {
            for (Customer c : customers) {
    %>
    <tr>
        <td><%= c.getAccountNo() %></td>
        <td><%= c.getName() %></td>
        <td><%= c.getAddress() %></td>
        <td><%= c.getPhone() %></td>
        <td><%= c.getUnitsConsumed() %></td>
    </tr>
    <%
        }
    } else {
    %>
    <tr><td colspan="5">No customers found.</td></tr>
    <% } %>
</table>

</body>
</html>
