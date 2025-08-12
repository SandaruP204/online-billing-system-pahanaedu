<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Bill Created</title>
</head>
<body>
<h1>Success!</h1>
<p><%= request.getParameter("msg") != null ? request.getParameter("msg") : "Bill created successfully." %></p>
<a href="addBill.jsp">Add Another Bill</a>
</body>
</html>
