<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.Customer, model.Product" %>

<%!
  // HTML escape for server-side output
  private static String h(String s){
    if (s == null) return "";
    return s.replace("&","&amp;")
            .replace("<","&lt;")
            .replace(">","&gt;")
            .replace("\"","&quot;")
            .replace("'","&#39;");
  }
%>

<%
  List<Customer> customers = (List<Customer>) request.getAttribute("customers");
  List<Product>  products  = (List<Product>)  request.getAttribute("products");
  if (customers == null) customers = new ArrayList<>();
  if (products  == null) products  = new ArrayList<>();
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Add Bill</title>
  <link rel="stylesheet" href="css/add-bill.css">
</head>
<body>
<div class="page">
  <h1>Create Bill</h1>

  <form class="card" id="billForm" action="addBill" method="post">
    <!-- Customer -->
    <div class="field">
      <label for="customerInput">Customer</label>
      <input id="customerInput" type="text" placeholder="Type account no or name…" list="customersList" autocomplete="off">
      <datalist id="customersList">
        <% for (Customer c : customers) { %>
        <option value="<%= c.getAccountNo() %> - <%= h(c.getName()) %>"><%= h(c.getAddress()) %></option>
        <% } %>
      </datalist>
      <!-- Hidden value your servlet reads -->
      <input type="hidden" name="accountNo" id="accountNo">
      <div id="customerPicked" class="picked hidden"></div>
    </div>

    <hr/>

    <!-- Products -->
    <div class="field">
      <label>Products</label>
      <div id="rows" class="rows"></div>
      <button type="button" class="btn ghost" id="addRowBtn">+ Add product</button>
    </div>

    <div class="totals">
      <span>Estimated total:</span>
      <strong id="grandTotal">0.00</strong>
    </div>

    <div class="actions">
      <button type="submit" class="btn primary">Create Bill</button>
    </div>
  </form>
</div>

<!-- Hidden product data (DOM dataset, avoids mixing JSP inside JS) -->
<div id="productData" style="display:none">
  <% for (Product p : products) { %>
  <div class="p"
       data-no="<%= p.getProductNo() %>"
       data-name="<%= h(p.getName()) %>"
       data-unit="<%= p.getUnit() %>"
       data-price="<%= p.getPrice() %>"></div>
  <% } %>
</div>

<script>
  // Build PRODUCTS from DOM data (no JSP inside JS)
  (function(){
    var nodes = document.querySelectorAll('#productData .p');
    var list = [];
    for (var i=0;i<nodes.length;i++){
      var n = nodes[i];
      list.push({
        no: parseInt(n.getAttribute('data-no'),10),
        name: n.getAttribute('data-name') || '',
        unit: parseInt(n.getAttribute('data-unit')||'0',10),
        price: parseFloat(n.getAttribute('data-price')||'0')
      });
    }
    window.PRODUCTS = list;
  })();

  // client-side HTML escaper for dynamic UI
  function escapeHtml(str){
    return (str||"").replace(/[&<>"']/g, function(m){ return {'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]; });
  }

  // ===== Customer handling =====
  var custInput = document.getElementById('customerInput');
  var accountNo = document.getElementById('accountNo');
  var custPicked= document.getElementById('customerPicked');

  custInput.addEventListener('change', function () {
    // Expecting "12345 - Name"
    var m = custInput.value.match(/^(\d+)\s*-\s*(.+)$/);
    if (m) {
      accountNo.value = m[1];
      custPicked.classList.remove('hidden');
      custPicked.innerHTML = "<b>Selected:</b> #" + m[1] + " — " + escapeHtml(m[2]);
    } else {
      accountNo.value = '';
      custPicked.classList.add('hidden');
      custPicked.textContent = '';
    }
  });

  // ===== Product rows =====
  var rows = document.getElementById('rows');
  document.getElementById('addRowBtn').addEventListener('click', addRow);

  // Build products datalist once (from PRODUCTS)
  var dl = document.createElement('datalist');
  dl.id = 'productsList';
  dl.innerHTML = (window.PRODUCTS || []).map(function(p){
    var priceStr = isFinite(p.price) ? Number(p.price).toFixed(2) : '0.00';
    return '<option value="' + p.no + ' - ' + escapeHtml(p.name) + ' (Rs ' + priceStr + ', Stock ' + p.unit + ')"></option>';
  }).join('');
  document.body.appendChild(dl);

  // Add initial row
  addRow();

  function addRow(){
    var row = document.createElement('div');
    row.className = 'row';
    row.innerHTML =
            '<input type="text" class="prodInput" placeholder="Type product no or name…" list="productsList" autocomplete="off">' +
            '<input type="hidden" name="productNo" class="prodNo">' +
            '<input type="text" class="prodName" placeholder="Product name" readonly>' +
            '<input type="number" class="prodPrice" placeholder="Price" step="0.01" min="0" readonly>' +
            '<input type="number" name="quantity" class="qty" placeholder="Qty" min="1" value="1">' +
            '<span class="lineTotal">0.00</span>' +
            '<button type="button" class="icon remove" title="Remove">&times;</button>';
    rows.appendChild(row);

    var prodInput = row.querySelector('.prodInput');
    var prodNo    = row.querySelector('.prodNo');
    var prodName  = row.querySelector('.prodName');
    var prodPrice = row.querySelector('.prodPrice');
    var qty       = row.querySelector('.qty');
    var lineTotal = row.querySelector('.lineTotal');
    var removeBtn = row.querySelector('.remove');

    prodInput.addEventListener('change', function () {
      // Expecting "1001 - Name (..."
      var m = prodInput.value.match(/^(\d+)\s*-\s*(.+)$/);
      if (!m) { clearRow(); updateTotals(); return; }
      var no = parseInt(m[1],10);
      var p = (window.PRODUCTS || []).find(function(pp){ return pp.no === no; });
      if (!p) { clearRow(); updateTotals(); return; }
      prodNo.value    = p.no;
      prodName.value  = p.name;
      prodPrice.value = Number(p.price||0).toFixed(2);
      updateLine();
    });

    qty.addEventListener('input', function () {
      if (qty.value === '' || parseInt(qty.value,10) < 1) qty.value = 1;
      updateLine();
    });

    removeBtn.addEventListener('click', function () {
      row.remove();
      updateTotals();
    });

    function clearRow(){
      prodNo.value = '';
      prodName.value = '';
      prodPrice.value = '';
      lineTotal.textContent = '0.00';
    }

    function updateLine(){
      var q = parseInt(qty.value || '1',10);
      var pr = parseFloat(prodPrice.value || '0');
      lineTotal.textContent = (q * pr).toFixed(2);
      updateTotals();
    }
  }

  function updateTotals(){
    var sum = 0;
    var lines = rows.querySelectorAll('.lineTotal');
    for (var i=0;i<lines.length;i++){
      var v = parseFloat(lines[i].textContent || '0');
      if (!isNaN(v)) sum += v;
    }
    document.getElementById('grandTotal').textContent = sum.toFixed(2);
  }

  // Final client-side sanity
  document.getElementById('billForm').addEventListener('submit', function (e) {
    if (!accountNo.value) {
      e.preventDefault(); alert('Please choose a customer from the list.'); return;
    }
    var productNos = Array.prototype.map.call(
            document.querySelectorAll('input[name="productNo"]'),
            function(i){ return i.value; }
    );
    var qtys = Array.prototype.map.call(
            document.querySelectorAll('input[name="quantity"]'),
            function(i){ return parseInt(i.value||'0',10); }
    );
    if (productNos.length===0 || productNos.some(function(v){ return !v; }) ||
            qtys.length===0 || qtys.some(function(v){ return v<1; })) {
      e.preventDefault(); alert('Please pick at least one product and valid quantities.'); return;
    }
  });
</script>
</body>
</html>
