<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Add New Bill</title>
  <script>
    // Add new row to the items table
    function addItemRow() {
      const table = document.getElementById("itemsTable");
      const row = table.insertRow(-1);

      const cell1 = row.insertCell(0);
      const productInput = document.createElement("input");
      productInput.type = "number";
      productInput.name = "productNo";
      productInput.required = true;
      productInput.min = 1;
      cell1.appendChild(productInput);

      const cell2 = row.insertCell(1);
      const quantityInput = document.createElement("input");
      quantityInput.type = "number";
      quantityInput.name = "quantity";
      quantityInput.required = true;
      quantityInput.min = 1;
      cell2.appendChild(quantityInput);

      const cell3 = row.insertCell(2);
      const removeBtn = document.createElement("button");
      removeBtn.type = "button";
      removeBtn.innerText = "Remove";
      removeBtn.onclick = function() {
        table.deleteRow(row.rowIndex);
      };
      cell3.appendChild(removeBtn);
    }
  </script>
</head>
<body>
<h1>Add New Bill</h1>
<form action="addBill" method="post">
  <label for="accountNo">Customer Account Number:</label>
  <input type="number" name="accountNo" id="accountNo" required min="1"><br><br>

  <table id="itemsTable" border="1" cellpadding="5">
    <thead>
    <tr>
      <th>Product No</th>
      <th>Quantity</th>
      <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <tr>
      <td><input type="number" name="productNo" required min="1"></td>
      <td><input type="number" name="quantity" required min="1"></td>
      <td><button type="button" onclick="this.closest('tr').remove()">Remove</button></td>
    </tr>
    </tbody>
  </table><br>

  <button type="button" onclick="addItemRow()">Add Another Item</button><br><br>

  <input type="submit" value="Create Bill">
</form>
</body>
</html>
