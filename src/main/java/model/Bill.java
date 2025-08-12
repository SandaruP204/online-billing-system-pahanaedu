package model;

import java.util.Date;
import java.util.List;

public class Bill {
    private int billId;
    private int accountNo;  // links to customers.accountNo
    private Date billDate;
    private List<BillItem> items;

    public Bill() {}

    public Bill(int billId, int accountNo, Date billDate, List<BillItem> items) {
        this.billId = billId;
        this.accountNo = accountNo;
        this.billDate = billDate;
        this.items = items;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public List<BillItem> getItems() {
        return items;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
    }

    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    private double totalAmount;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
