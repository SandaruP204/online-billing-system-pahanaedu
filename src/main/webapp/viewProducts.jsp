<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Product" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>View Products</title>
</head>
<body>
<h1>All Products</h1>

<%
    // Declare once here
    List<Product> products = (List<Product>) request.getAttribute("products");

    // Debug output
    if (products == null) {
        out.println("<p><b>Debug:</b> 'products' attribute is <span style='color:red;'>null</span>.</p>");
    } else if (products.isEmpty()) {
        out.println("<p><b>Debug:</b> Product list is <span style='color:orange;'>empty</span>.</p>");
    } else {
        out.println("<p><b>Debug:</b> Product list size: " + products.size() + "</p>");
    }
%>

<table border="1">
    <tr>
        <th>Product No</th>
        <th>Name</th>
        <th>Unit</th>
        <th>Price</th>
    </tr>

    <%
        if (products != null && !products.isEmpty()) {
            for (Product p : products) {
    %>
    <tr>
        <td><%= p.getProductNo() %></td>
        <td><%= p.getName() %></td>
        <td><%= p.getUnit() %></td>
        <td><%= p.getPrice() %></td>
    </tr>
    <%
        }
    } else {
    %>
    <tr><td colspan="3">No products found.</td></tr>
    <%
        }
    %>
</table>
</body>
</html>
