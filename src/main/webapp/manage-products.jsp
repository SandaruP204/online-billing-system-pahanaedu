<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Product, java.util.*, java.text.DecimalFormat" %>
<%!
  // simple HTML escape (no JSTL)
  private static String h(String s){
    if (s == null) return "";
    return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
            .replace("\"","&quot;").replace("'","&#39;");
  }
  private static String money(double v){
    // show 2 decimals, no grouping to keep it simple
    return new DecimalFormat("0.00").format(v);
  }
%>
<%
  List<Product> products = (List<Product>) request.getAttribute("products");
  if (products == null) products = new ArrayList<>();
  String editProductNo = request.getParameter("edit"); // row inline-editing
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Manage Products</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="css/manages-products.css">
</head>
<body>

<div class="wrap">
  <div class="header">
    <h1>Manage Products</h1>
    <a class="btn" href="viewProducts">↻ Refresh</a>
    <button type="button" class="btn primary"
            onclick="location.href='<%=request.getContextPath()%>/index.jsp'">
      ← Back to Home
    </button>
  </div>

  <!-- Add Product Card -->
  <div class="card">
    <h2>Add New Product</h2>
    <form action="AddProductServlet" method="post" class="form">
      <div class="grid">
        <div class="field">
          <label for="productNo">Product Number</label>
          <input id="productNo" type="number" name="productNo" required placeholder="" min="1">
        </div>
        <div class="field">
          <label for="name">Name</label>
          <input id="name" type="text" name="name" required placeholder="">
        </div>
        <div class="field">
          <label for="unit">Quantity (Stock)</label>
          <input id="unit" type="number" name="unit" required placeholder="" min="0">
        </div>
        <div class="field">
          <label for="price">Price</label>
          <input id="price" type="number" name="price" required placeholder="" min="0" step="0.01">
        </div>
      </div>
      <div class="actions">
        <button type="submit" class="btn primary">Add Product</button>
        <a href="viewProducts" class="btn ghost">Cancel</a>
      </div>
    </form>
  </div>

  <!-- Products Table Card -->
  <div class="card">
    <div class="table-toolbar">
      <h2>All Products</h2>
      <input id="q" type="text" placeholder="Search by name or number…">
    </div>

    <div class="table-wrap">
      <table id="productsTable">
        <thead>
        <tr>
          <th style="width:120px">Product No</th>
          <th>Name</th>
          <th style="width:140px" class="num">Unit</th>
          <th style="width:160px" class="num">Price</th>
          <th style="width:180px">Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
          if (!products.isEmpty()) {
            for (Product p : products) {
              boolean isEditing = (editProductNo != null) &&
                      editProductNo.equals(String.valueOf(p.getProductNo()));
        %>
        <tr>
          <% if (isEditing) { %>
          <form action="EditProductServlet" method="post">
            <td class="mono">#<%= p.getProductNo() %>
              <input type="hidden" name="productNo" value="<%= p.getProductNo() %>">
            </td>
            <td><input type="text" name="name" value="<%= h(p.getName()) %>" required></td>
            <td><input type="number" name="unit" value="<%= p.getUnit() %>" min="0" required></td>
            <td><input type="number" name="price" value="<%= money(p.getPrice()) %>" step="0.01" min="0" required></td>
            <td class="actions">
              <button type="submit" class="btn small primary">Save</button>
              <a class="btn small ghost" href="viewProducts">Cancel</a>
            </td>
          </form>
          <% } else { %>
          <td class="mono">#<%= p.getProductNo() %></td>
          <td><%= h(p.getName()) %></td>
          <td class="num mono"><%= p.getUnit() %></td>
          <td class="num mono">Rs. <%= money(p.getPrice()) %></td>
          <td class="actions">
            <a class="btn small ghost" href="viewProducts?edit=<%= p.getProductNo() %>">Edit</a>
            <a class="btn small danger"
               href="DeleteProductServlet?productNo=<%= p.getProductNo() %>"
               onclick="return confirm('Delete product #<%= p.getProductNo() %>?')">Delete</a>
          </td>
          <% } %>
        </tr>
        <%
          }
        } else {
        %>
        <tr><td colspan="5" class="empty">No products found.</td></tr>
        <%
          }
        %>
        </tbody>
      </table>
    </div>
  </div>
</div>

<script>
  // simple client-side search
  (function(){
    var q = document.getElementById('q');
    var tbody = document.querySelector('#productsTable tbody');
    if (!q || !tbody) return;
    q.addEventListener('input', function(){
      var term = (q.value || '').toLowerCase().trim();
      var rows = tbody.querySelectorAll('tr');
      for (var i=0;i<rows.length;i++){
        var tr = rows[i];
        if (tr.querySelector('.empty')) continue;
        var id  = (tr.cells[0]?.innerText || '').toLowerCase();
        var name= (tr.cells[1]?.innerText || '').toLowerCase();
        tr.style.display = (!term || id.indexOf(term)>=0 || name.indexOf(term)>=0) ? '' : 'none';
      }
    });
  })();
</script>
</body>
</html>
