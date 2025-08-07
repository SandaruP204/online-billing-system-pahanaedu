<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>

<html>
<head>
    <title>Manage Products</title>
    <link rel="stylesheet" href="css/manage-products.css">
</head>
<body>
<h1>All Products</h1>

<table border="1" cellpadding="8" cellspacing="0">
    <tr>
        <th>Product No</th>
        <th>Name</th>
        <th>Unit</th>
        <th>Price</th>
        <th>Actions</th>
    </tr>

    <%
        List<Product> products = (List<Product>) request.getAttribute("products");
        String editProductNo = request.getParameter("edit");
        if (products != null && !products.isEmpty()) {
            for (Product p : products) {
                boolean isEditing = Objects.equals(editProductNo, String.valueOf(p.getProductNo()));
    %>
    <tr>
        <%
            if (isEditing) {
        %>
        <form action="EditProductServlet" method="post">
            <td><%= p.getProductNo() %>
                <input type="hidden" name="productNo" value="<%= p.getProductNo() %>">
            </td>
            <td><input type="text" name="name" value="<%= p.getName() %>" required></td>
            <td><input type="number" name="unit" value="<%= p.getUnit() %>" required></td>
            <td><input type="text" name="price" value="<%= p.getPrice() %>" required></td>
            <td>
                <button type="submit">Save</button>
                <a href="viewProducts">Cancel</a>
            </td>
        </form>
        <%
        } else {
        %>
        <td><%= p.getProductNo() %></td>
        <td><%= p.getName() %></td>
        <td><%= p.getUnit() %></td>
        <td><%= p.getPrice() %></td>
        <td>
            <a href="viewProducts?edit=<%= p.getProductNo() %>">Edit</a> |
            <a href="DeleteProductServlet?productNo=<%= p.getProductNo() %>" onclick="return confirm('Are you sure you want to delete this product?')">Delete</a>
        </td>
        <%
            }
        %>
    </tr>
    <%
        }
    } else {
    %>
    <tr><td colspan="5">No products found.</td></tr>
    <%
        }
    %>
</table>
</body>
</html>
