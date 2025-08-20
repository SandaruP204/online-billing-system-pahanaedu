<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Help & Guide</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/help.css">
</head>
<body>
<div class="wrap">
    <header class="top">
        <h1>Help & User Guide</h1>
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

        <button class="btn primary" onclick="window.print()">Print</button>
    </header>

    <section class="card">
        <h2>Login</h2>
        <ol>
            <li>Enter your username and password on the Login page.</li>
            <li>Admins go to Admin Dashboard; Employers go to Employer Dashboard.</li>
        </ol>
    </section>

    <section class="card">
        <h2>Manage Customers</h2>
        <ul>
            <li><b>Add Customer:</b> Fill the form and submit.</li>
            <li><b>Edit:</b> Click “Edit” on a row, change values, then “Save”.</li>
            <li><b>Delete:</b> Click “Delete” (with confirmation).</li>
            <li>All fields validate (e.g., phone format, non-negative units).</li>
        </ul>
    </section>

    <section class="card">
        <h2>Manage Products</h2>
        <ul>
            <li><b>Add Product:</b> Provide product number, name, stock, price.</li>
            <li><b>Edit/Delete:</b> Use actions on each row.</li>
            <li>Validation ensures non-negative stock and price.</li>
        </ul>
    </section>

    <section class="card">
        <h2>Create Bill</h2>
        <ol>
            <li>Choose a customer by typing account number or name (auto-suggest).</li>
            <li>Add products; quantities must be positive and ≤ stock.</li>
            <li>Submit to generate a bill; you can print the bill details view.</li>
        </ol>
    </section>

    <section class="card">
        <h2>Reports</h2>
        <ul>
            <li>View bills within a date range to analyze totals.</li>
            <li>Use the “Print” button for hard copies.</li>
        </ul>
    </section>

    <footer class="muted foot">
        © <script>document.write(new Date().getFullYear())</script> Pahana Utilities
    </footer>
</div>
</body>
</html>
