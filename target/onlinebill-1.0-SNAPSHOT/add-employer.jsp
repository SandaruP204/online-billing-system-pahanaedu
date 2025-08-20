<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    // Simple HTML escape (no JSTL / libs)
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
    <title>Add Employer</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/add-employer.css">
</head>
<body>
<div class="wrap">
    <div class="card">
        <div class="header">
            <h2>Add Employer</h2>
            <a class="btn ghost" href="index.jsp">Back</a>
        </div>

        <form action="addEmployer" method="post" class="form">
            <div class="field">
                <label for="username">Username</label>
                <input id="username" type="text" name="username" required placeholder="Enter username">
            </div>

            <div class="field">
                <label for="password">Password</label>
                <div class="password-row">
                    <input id="password" type="password" name="password" required placeholder="Enter password">
                    <button type="button" class="toggle" aria-label="Show/Hide password" onclick="togglePw()">👁</button>
                </div>
                <small class="hint">Tip: use at least 8 chars with letters & numbers.</small>
            </div>

            <div class="actions">
                <button type="submit" class="btn primary">Add Employer</button>
                <a href="index.jsp" class="btn ghost">Cancel</a>
            </div>
        </form>

        <%
            String msg = request.getParameter("msg");
            if (msg != null && !msg.isBlank()) {
        %>
        <div class="alert success"><%= h(msg) %></div>
        <% } %>
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
