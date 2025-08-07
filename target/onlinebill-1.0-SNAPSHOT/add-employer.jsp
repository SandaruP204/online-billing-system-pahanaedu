<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Add Employer</title>
</head>
<body>
<h2>Add Employer</h2>

<form action="addEmployer" method="post">
  <label>Email:</label><br>
  <input type="text" name="username" required><br><br>

  <label>Password:</label><br>
  <input type="password" name="password" required><br><br>

  <button type="submit">Add Employer</button>
</form>

<%
  String msg = request.getParameter("msg");
  if (msg != null) {
%>
<p style="color: green;"><%= msg %></p>
<% } %>
</body>
</html>
