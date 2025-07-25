<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Product" %>

<html>
<head>
  <title>Buy Product</title>
</head>
<body>
<h2>Buy Product</h2>

<form action="buyProduct" method="post">
  <label>Product:</label>
  <select name="productNo">
    <%
      List<Product> products = (List<Product>) request.getAttribute("products");
      if (products != null) {
        for (Product p : products) {
    %>
    <option value="<%= p.getProductNo() %>">
      <%= p.getName() %> (Available: <%= p.getUnit() %>, Price: $<%= p.getPrice() %>)
    </option>
    <%
      }
    } else {
    %>
    <option>No products found</option>
    <% } %>
  </select><br><br>

  <label>Quantity:</label>
  <input type="number" name="quantity" required><br><br>

  <button type="submit">Buy</button>
</form>

<% if (request.getAttribute("error") != null) { %>
<p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>

</body>
</html>
