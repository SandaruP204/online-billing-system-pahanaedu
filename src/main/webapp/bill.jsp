<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
  <title>Your Bill</title>
</head>
<body>
<h2>Thank you for your purchase!</h2>

<p>Your total amount is: $<%= request.getAttribute("total") %></p>

<a href="buyProduct">Buy Another</a>

</body>
</html>
