<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Customer" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>

<html>
<head>
  <title>Manage Customers</title>
  <link rel="stylesheet" href="css/customer-style.css">
</head>
<body>
<h1>All Customers</h1>

<table border="1">
  <tr>
    <th>Account No</th>
    <th>Name</th>
    <th>Address</th>
    <th>Phone</th>
    <th>Units Consumed</th>
    <th>Actions</th>
  </tr>

  <%
    List<Customer> customers = (List<Customer>) request.getAttribute("customers");
    String editId = request.getParameter("edit");
    if (customers != null && !customers.isEmpty()) {
      for (Customer c : customers) {
        boolean isEditing = Objects.equals(editId, String.valueOf(c.getAccountNo()));
  %>
  <tr>
    <%
      if (isEditing) {
    %>
    <form action="UpdateCustomerServlet" method="post">
      <td><%= c.getAccountNo() %><input type="hidden" name="accountNo" value="<%= c.getAccountNo() %>" /></td>
      <td><input type="text" name="name" value="<%= c.getName() %>" required></td>
      <td><input type="text" name="address" value="<%= c.getAddress() %>" required></td>
      <td><input type="text" name="phone" value="<%= c.getPhone() %>" required></td>
      <td><input type="number" name="unitsConsumed" value="<%= c.getUnitsConsumed() %>" required></td>
      <td>
        <button type="submit">Save</button>
        <a href="ViewCustomerServlet">Cancel</a>
      </td>
    </form>
    <%
    } else {
    %>
    <td><%= c.getAccountNo() %></td>
    <td><%= c.getName() %></td>
    <td><%= c.getAddress() %></td>
    <td><%= c.getPhone() %></td>
    <td><%= c.getUnitsConsumed() %></td>
    <td>
      <a href="ViewCustomerServlet?edit=<%= c.getAccountNo() %>">Edit</a> |
      <a href="${pageContext.request.contextPath}/DeleteCustomerServlet?accountNo=<%= c.getAccountNo() %>" onclick="return confirm('Are you sure?')">Delete</a>
    </td>
    <%
      }
    %>
  </tr>
  <%
    }
  } else {
  %>
  <tr><td colspan="6">No customers found.</td></tr>
  <% } %>
</table>

</body>
</html>
