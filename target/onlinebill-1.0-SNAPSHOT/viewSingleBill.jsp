<%@ page import="model.Bill, java.util.List, model.BillItemDetails" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bill Details</title>
</head>
<body>
<h1>Bill Details</h1>

<%
    Bill bill = (Bill) request.getAttribute("bill");
    List<BillItemDetails> items = (List<BillItemDetails>) request.getAttribute("items");

    if (bill != null) {
%>
<p><strong>Bill ID:</strong> <%= bill.getBillId() %></p>
<p><strong>Customer:</strong> <%= bill.getCustomerName() %></p>
<p><strong>Date:</strong> <%= bill.getBillDate() %></p>
<p><strong>Total:</strong> $<%= bill.getTotalAmount() %></p>

<h2>Items</h2>
<table border="1">
    <thead>
    <tr>
        <th>Product Name</th>
        <th>Quantity</th>
        <th>Unit Price</th>
        <th>Total</th>
    </tr>
    </thead>
    <tbody>
    <%
        if (items != null) {
            for (BillItemDetails item : items) {
    %>
    <tr>
        <td><%= item.getProductName() %></td>
        <td><%= item.getQuantity() %></td>
        <td>$<%= item.getUnitPrice() %></td>
        <td>$<%= item.getTotal() %></td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>
<%
} else {
%>
<p>No bill found.</p>
<%
    }
%>

</body>
</html>
