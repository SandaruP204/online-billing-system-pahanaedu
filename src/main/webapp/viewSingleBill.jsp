<%@ page import="model.Bill, java.util.List, model.BillItemDetails, java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%!
    private static String h(String s){
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
                .replace("\"","&quot;").replace("'","&#39;");
    }
    private static String fmtDate(java.util.Date d){
        if (d == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(d);
    }
    private static String money(double v){
        return String.format("%.2f", v);
    }
%>

<%
    Bill bill = (Bill) request.getAttribute("bill");
    List<BillItemDetails> items = (List<BillItemDetails>) request.getAttribute("items");
%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Bill Details</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bill-details.css">
</head>
<body>
<div class="print-header print-only">
    <div class="brand">Pahana Utilities</div>
    <% if (bill != null) { %>
    <div class="meta">Invoice #<%= bill.getBillId() %> • <%= h(bill.getCustomerName()) %> • <%= fmtDate(bill.getBillDate()) %></div>
    <% } %>
</div>

<div class="wrap">
    <div class="header">
        <h1>Bill Details</h1>
        <div class="actions no-print">
            <a class="btn" href="viewBills">← Back to Bills</a>
            <button type="button" class="btn" onclick="window.print()">🖨 Print</button>
        </div>
    </div>

    <% if (bill != null) { %>
    <div class="card summary">
        <div class="summary-row">
            <div class="kv">
                <span class="k">Bill ID</span>
                <span class="v mono">#<%= bill.getBillId() %></span>
            </div>
            <div class="kv">
                <span class="k">Customer</span>
                <span class="v"><%= h(bill.getCustomerName()) %></span>
            </div>
            <div class="kv">
                <span class="k">Date</span>
                <span class="v mono"><%= fmtDate(bill.getBillDate()) %></span>
            </div>
            <div class="kv">
                <span class="k">Total</span>
                <span class="v amt">Rs. <%= money(bill.getTotalAmount()) %></span>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Product</th>
                    <th class="num">Qty</th>
                    <th class="num">Unit Price</th>
                    <th class="num">Line Total</th>
                </tr>
                </thead>
                <tbody>
                <% if (items != null && !items.isEmpty()) {
                    for (BillItemDetails it : items) { %>
                <tr>
                    <td><%= h(it.getProductName()) %></td>
                    <td class="num mono"><%= it.getQuantity() %></td>
                    <td class="num mono">Rs. <%= money(it.getUnitPrice()) %></td>
                    <td class="num mono strong">Rs. <%= money(it.getTotal()) %></td>
                </tr>
                <% } } else { %>
                <tr>
                    <td colspan="4" class="empty">No items in this bill.</td>
                </tr>
                <% } %>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="3" class="right strong">Grand Total</td>
                    <td class="num mono strong">Rs. <%= money(bill.getTotalAmount()) %></td>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
    <% } else { %>
    <div class="card empty-card">
        <p class="empty">No bill found.</p>
        <a class="btn" href="viewBills">Back to Bills</a>
    </div>
    <% } %>
</div>
</body>
</html>
