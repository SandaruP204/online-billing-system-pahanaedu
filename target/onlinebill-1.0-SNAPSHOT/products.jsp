<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Product" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Manage Products</title>
</head>
<body>
<h1>All Products</h1>

<%
    List<Product> products = (List<Product>) request.getAttribute("products");
%>

<table border="1" cellpadding="8" cellspacing="0">
    <tr>
        <th>Product No</th>
        <th>Name</th>
        <th>Unit</th>
        <th>Price</th>
        <th>Actions</th>
    </tr>

    <%
        if (products != null && !products.isEmpty()) {
            for (Product p : products) {
    %>
    <tr>
        <form action="EditProductServlet" method="post">
            <td>
                <input type="text" name="productNo" value="<%= p.getProductNo() %>" readonly>
            </td>
            <td>
                <input type="text" name="name" value="<%= p.getName() %>">
            </td>
            <td>
                <input type="number" name="unit" value="<%= p.getUnit() %>">
            </td>
            <td>
                <input type="text" name="price" value="<%= p.getPrice() %>">
            </td>
            <td>
                <input type="submit" value="Save">
                <a href="DeleteProductServlet?productNo=<%= p.getProductNo() %>"
                   onclick="return confirm('Are you sure you want to delete this product?')">Delete</a>
            </td>
        </form>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="5">No products found.</td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
