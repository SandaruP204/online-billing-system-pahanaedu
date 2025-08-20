<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Customer, java.util.*" %>
<%!
  // simple HTML escape
  private static String h(String s){
    if (s == null) return "";
    return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
            .replace("\"","&quot;").replace("'","&#39;");
  }
%>
<%
  List<Customer> customers = (List<Customer>) request.getAttribute("customers");
  if (customers == null) customers = new ArrayList<>();
  String editId = request.getParameter("edit");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Manage Customers</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="css/manage-customers.css">
</head>
<body>
<button type="button"
        onclick="location.href='<%=request.getContextPath()%>/index.jsp'">
  ← Back to Home
</button>

<div class="wrap">
  <div class="header">
    <h1>Manage Customers</h1>
    <a class="btn" href="ViewCustomerServlet">↻ Refresh</a>
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

  </div>

  <!-- Add Customer -->
  <div class="card">
    <h2>Add New Customer</h2>
    <form action="AddCustomerServlet" method="post" class="form">
      <div class="grid">
        <div class="field">
          <label for="accountNo">Account Number</label>
          <input id="accountNo" type="number" name="accountNo" required placeholder="" min="1">
        </div>

        <div class="field">
          <label for="name">Name</label>
          <input id="name" type="text" name="name" required placeholder="">
        </div>

        <div class="field">
          <label for="address">Address</label>
          <input id="address" type="text" name="address" required placeholder="">
        </div>

        <div class="field">
          <label for="phone">Phone</label>
          <input id="phone" type="text" name="phone" required placeholder="">
        </div>

        <div class="field">
          <label for="unitsConsumed">Units Consumed</label>
          <input id="unitsConsumed" type="number" name="unitsConsumed" required placeholder="" min="0">
        </div>
      </div>

      <div class="actions">
        <button type="submit" class="btn primary">Add Customer</button>
        <a href="ViewCustomerServlet" class="btn ghost">Cancel</a>
      </div>
    </form>
  </div>

  <!-- Customers Table -->
  <div class="card">
    <div class="table-toolbar">
      <h2>All Customers</h2>
      <input id="q" type="text" placeholder="Search by name, account, phone…">
    </div>

    <div class="table-wrap">
      <table id="customersTable">
        <thead>
        <tr>
          <th style="width:140px">Account No</th>
          <th>Name</th>
          <th>Address</th>
          <th style="width:160px">Phone</th>
          <th style="width:150px" class="num">Units</th>
          <th style="width:180px">Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
          if (!customers.isEmpty()) {
            for (Customer c : customers) {
              boolean isEditing = (editId != null) && editId.equals(String.valueOf(c.getAccountNo()));
        %>
        <tr>
          <% if (isEditing) { %>
          <form action="UpdateCustomerServlet" method="post">
            <td class="mono">#<%= c.getAccountNo() %>
              <input type="hidden" name="accountNo" value="<%= c.getAccountNo() %>">
            </td>
            <td><input type="text" name="name" value="<%= h(c.getName()) %>" required></td>
            <td><input type="text" name="address" value="<%= h(c.getAddress()) %>" required></td>
            <td><input type="text" name="phone" value="<%= h(c.getPhone()) %>" required></td>
            <td class="num"><input type="number" name="unitsConsumed" value="<%= c.getUnitsConsumed() %>" min="0" required></td>
            <td class="actions">
              <button type="submit" class="btn small primary">Save</button>
              <a class="btn small ghost" href="ViewCustomerServlet">Cancel</a>
            </td>
          </form>
          <% } else { %>
          <td class="mono">#<%= c.getAccountNo() %></td>
          <td><%= h(c.getName()) %></td>
          <td><%= h(c.getAddress()) %></td>
          <td><%= h(c.getPhone()) %></td>
          <td class="num mono"><%= c.getUnitsConsumed() %></td>
          <td class="actions">
            <a class="btn small ghost" href="ViewCustomerServlet?edit=<%= c.getAccountNo() %>">Edit</a>
            <a class="btn small danger"
               href="DeleteCustomerServlet?accountNo=<%= c.getAccountNo() %>"
               onclick="return confirm('Delete customer #<%= c.getAccountNo() %>?')">Delete</a>
          </td>
          <% } %>
        </tr>
        <%
          }
        } else {
        %>
        <tr><td colspan="6" class="empty">No customers found.</td></tr>
        <%
          }
        %>
        </tbody>
      </table>
    </div>
  </div>
</div>

<script>
  // simple client-side filter
  (function(){
    var q = document.getElementById('q');
    var tbody = document.querySelector('#customersTable tbody');
    if (!q || !tbody) return;
    q.addEventListener('input', function(){
      var term = (q.value || '').toLowerCase().trim();
      var rows = tbody.querySelectorAll('tr');
      for (var i=0;i<rows.length;i++){
        var tr = rows[i];
        if (tr.querySelector('.empty')) continue;
        var acc  = (tr.cells[0]?.innerText || '').toLowerCase();
        var name = (tr.cells[1]?.innerText || '').toLowerCase();
        var addr = (tr.cells[2]?.innerText || '').toLowerCase();
        var phone= (tr.cells[3]?.innerText || '').toLowerCase();
        var show = !term || acc.indexOf(term)>=0 || name.indexOf(term)>=0 ||
                addr.indexOf(term)>=0 || phone.indexOf(term)>=0;
        tr.style.display = show ? '' : 'none';
      }
    });
  })();
</script>
</body>
</html>
