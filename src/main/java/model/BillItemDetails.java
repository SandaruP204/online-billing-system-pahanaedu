package model;

public class BillItemDetails {
    private String productName;
    private int quantity;
    private double unitPrice;
    private double total;

    // Getter methods
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotal() { return total; }

    // Setter methods
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
