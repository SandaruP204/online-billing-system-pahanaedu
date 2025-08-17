<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Your Products!</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/addproducts.css">
</head>
<body>
<div class="wrap">
    <div class="card">
        <div class="header">
            <h2>Add New Product</h2>
        </div>

        <form action="AddProductServlet" method="post" class="form">
            <div class="field">
                <label for="productNo">Product Number</label>
                <input id="productNo" type="number" name="productNo" required placeholder="e.g. 1001" min="1">
            </div>

            <div class="field">
                <label for="name">Name</label>
                <input id="name" type="text" name="name" required placeholder="e.g. LED Bulb 9W">
            </div>

            <div class="field">
                <label for="unit">Quantity (Stock)</label>
                <input id="unit" type="number" name="unit" required placeholder="e.g. 50" min="0">
            </div>

            <div class="field">
                <label for="price">Price</label>
                <input id="price" type="number" name="price" required placeholder="e.g. 499.90" min="0" step="0.01">
            </div>

            <div class="actions">
                <button type="submit" class="btn primary">Add Product</button>
                <a href="viewProducts" class="btn ghost">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
