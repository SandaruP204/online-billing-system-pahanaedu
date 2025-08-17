<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    // simple HTML escape (avoid JSTL/libs)
    private static String h(String s){
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
                .replace("\"","&quot;").replace("'","&#39;");
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/stylesLogin.css">
</head>
<body>
<div class="wrap">
    <div class="card">
        <div class="header">
            <h1>Welcome back</h1>
            <p class="muted">Sign in to continue</p>
        </div>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null && !error.isBlank()) {
        %>
        <div class="alert error"><%= h(error) %></div>
        <% } %>

        <form action="login" method="post" class="form">
            <div class="field">
                <label for="username">Username</label>
                <input id="username" type="text" name="username" required placeholder="Enter your username">
            </div>

            <div class="field">
                <label for="password">Password</label>
                <div class="password-row">
                    <input id="password" type="password" name="password" required placeholder="Enter your password">
                    <button type="button" class="toggle" onclick="togglePw()" aria-label="Show/Hide password">👁</button>
                </div>
            </div>

            <div class="actions">
                <button type="submit" class="btn primary">Login</button>
            </div>
        </form>

        <div class="foot muted">
            <small>© <script>document.write(new Date().getFullYear())</script> Pahana Utilities</small>
        </div>
    </div>
</div>

<script>
    function togglePw(){
        var p = document.getElementById('password');
        p.type = (p.type === 'password') ? 'text' : 'password';
    }
</script>
</body>
</html>
