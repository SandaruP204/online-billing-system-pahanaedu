<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, java.math.BigDecimal" %>
<%@ page import="servlet.MonthlyReportServlet.DailyTotal" %>
<%@ page import="servlet.MonthlyReportServlet.TopItem" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Monthly Report</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reports.css">
</head>
<body>
<div class="wrap">

  <!-- Top bar -->
  <div class="topbar">
    <div class="brand">Pahana Edu • Reports</div>
    <div class="user">
      <span class="hello">Hi <%= (session.getAttribute("username")!=null?session.getAttribute("username"):"there") %> 👋</span>
      <a class="btn ghost" href="${pageContext.request.contextPath}/index.jsp">← Back to Home</a>
    </div>
  </div>

  <!-- Hero -->
  <section class="hero">
    <h1>Monthly Report</h1>
    <div class="muted">Select a month to view totals, daily performance, and top items.</div>
  </section>

  <!-- Filter bar -->
  <form class="bar" action="${pageContext.request.contextPath}/reports/monthly" method="get">
    <label>
      <span class="muted">Month</span>
      <input class="input" type="month" name="month" value="<%= (String)request.getAttribute("month") %>">
    </label>
    <button class="btn accent" type="submit">Show</button>
  </form>

  <% if (request.getAttribute("error") != null) { %>
  <div class="alert error"><%= request.getAttribute("error") %></div>
  <% } %>

  <!-- KPI cards -->
  <div class="grid">
    <div class="card">
      <div class="icon">💰</div>
      <h3>Total revenue</h3>
      <div class="value"><%= (BigDecimal) request.getAttribute("total") %></div>
    </div>
    <div class="card">
      <div class="icon">🧾</div>
      <h3>Total bills</h3>
      <div class="value"><%= (Integer) request.getAttribute("count") %></div>
    </div>
    <div class="card">
      <div class="icon">📈</div>
      <h3>Average bill</h3>
      <div class="value"><%= (BigDecimal) request.getAttribute("avg") %></div>
    </div>
  </div>

  <!-- Daily totals -->
  <div class="panel">
    <h3>Daily totals</h3>
    <div class="table-wrap">
      <table>
        <thead>
        <tr><th>Date</th><th>Bills</th><th>Revenue</th></tr>
        </thead>
        <tbody>
        <%
          List<DailyTotal> days = (List<DailyTotal>) request.getAttribute("daily");
          if (days != null && !days.isEmpty()) {
            for (DailyTotal d : days) {
        %>
        <tr>
          <td><%= d.day %></td>
          <td><%= d.bills %></td>
          <td><%= d.revenue %></td>
        </tr>
        <%
          }
        } else {
        %>
        <tr><td colspan="3" class="muted">No data.</td></tr>
        <%
          }
        %>
        </tbody>
      </table>
    </div>
  </div>

  <!-- Top items -->
  <div class="panel">
    <h3>Top 5 items (by quantity)</h3>
    <div class="muted" style="margin-bottom:8px;">Estimated revenue uses current item prices.</div>
    <div class="table-wrap">
      <table>
        <thead>
        <tr><th>Product</th><th>Qty</th><th>Estimated Revenue</th></tr>
        </thead>
        <tbody>
        <%
          List<TopItem> top = (List<TopItem>) request.getAttribute("topItems");
          if (top != null && !top.isEmpty()) {
            for (TopItem t : top) {
        %>
        <tr>
          <td>(#<%= t.productNo %>) <%= t.name %></td>
          <td><%= t.qty %></td>
          <td><%= t.estRevenue %></td>
        </tr>
        <%
          }
        } else {
        %>
        <tr><td colspan="3" class="muted">No items sold for this month.</td></tr>
        <%
          }
        %>
        </tbody>
      </table>
    </div>
  </div>

  <div class="foot muted">Report generated from live billing data.</div>
</div>

<!-- Prefill when opened directly -->
<script>
  (function(){
    var m = document.querySelector('input[type=month][name=month]');
    if (m && !m.value) {
      var d = new Date(), mm = String(d.getMonth()+1).padStart(2,'0');
      m.value = d.getFullYear() + '-' + mm;
    }
  })();
</script>
</body>
</html>
