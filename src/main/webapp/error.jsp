<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Error</title>
</head>
<body>
<h1>Error</h1>
<p><%= request.getParameter("msg") != null ? request.getParameter("msg") : "An error occurred." %></p>
<a href="addBill.jsp">Try Again</a>
</body>
</html>
