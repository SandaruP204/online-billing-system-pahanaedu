<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="css/style.css"> <!-- Removed Thymeleaf syntax -->
</head>
<body>
<h1>Welcome!</h1>
<br/>

<div class="wrapper">
    <h1>Login</h1>

    <!-- Show error message if it exists -->
    <p id="error-message" style="color:red;">
        <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
    </p>

    <!-- The actual form -->
    <form action="login" method="POST">
        <div>
            <label for="email-input">
                <span>@</span>
            </label>
            <input type="email" name="username" id="email-input" placeholder="Email" required>
        </div>
        <div>
            <label for="password-input"></label>
            <input type="password" name="password" id="password-input" placeholder="Password" required>
        </div>
        <button type="submit">Login</button>
    </form>
</div>

<a href="addCustomer.jsp">Hello Servlet</a>
</body>
</html>
