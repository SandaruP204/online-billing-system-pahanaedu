<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Bill, java.util.List" %>
<html>
<head>
  <title>View Bills</title>
</head>
<body>
<h2>All Bills</h2>
<table border="1">
  <tr>
    <th>Bill ID</th>
    <th>Customer</th>
    <th>Bill Date</th>
    <th>Total Amount</th>
    <th>Action</th>
  </tr>
  <%
    List<Bill> bills = (List<Bill>) request.getAttribute("bills");
    if (bills != null && !bills.isEmpty()) {
      for (Bill bill : bills) {
  %>
  <tr>
    <td><%= bill.getBillId() %></td>
    <td><%= bill.getCustomerName() %></td>
    <td><%= bill.getBillDate() %></td>
    <td>$<%= bill.getTotalAmount() %></td>
    <td><a href="viewBill?billId=<%= bill.getBillId() %>">View</a></td>
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="5">No bills found.</td>
  </tr>
  <%
    }
  %>
</table>
</body>
</html>
