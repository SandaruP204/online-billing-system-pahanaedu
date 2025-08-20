<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Bill, java.util.List" %>
<html>
<head>
  <title>View Bills</title>
  <link rel="stylesheet" href="css/view-bills.css">
</head>
<body>
<h2>All Bills</h2>
<%
  String role = (String) session.getAttribute("role");
  boolean isEmployer = role != null &&
          (role.equalsIgnoreCase("EMPLOYER") || role.equalsIgnoreCase("CASHIER"));
  String home = isEmployer ? "employer-dashboard.jsp" : "index.jsp";
%>
<button type="button" class="btn primary"
        onclick="location.href='<%=request.getContextPath()%>/<%= home %>'">
  ← Back to Home
</button>

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
