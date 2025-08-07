<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="css/styleLogin.css">
</head>
<body>
<div class="wrapper">
    <h1>Login</h1>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <p id="error-message"><%= error %></p>
    <%
        }
    %>

    <form action="login" method="post">
        <div class="input-box">
            <input type="text" name="username" placeholder="Email" required>
            <i class="bx bx-envelope"></i>
        </div>

        <div class="input-box">
            <input type="password" name="password" placeholder="Password" required>
            <i class="bx bx-lock-alt"></i>
        </div>

        <button type="submit" class="btn">Login</button>

    </form>
</div>
</body>
</html>
