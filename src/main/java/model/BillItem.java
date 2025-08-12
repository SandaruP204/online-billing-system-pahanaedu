package model;

public class BillItem {
    private int itemId;      // primary key in bill_items table
    private int billId;      // foreign key linking to bills.bill_id
    private int productNo;   // foreign key linking to products.productNo
    private int quantity;

    public BillItem() {}

    public BillItem(int itemId, int billId, int productNo, int quantity) {
        this.itemId = itemId;
        this.billId = billId;
        this.productNo = productNo;
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getProductNo() {
        return productNo;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
