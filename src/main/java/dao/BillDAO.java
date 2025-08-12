package dao;

import model.Bill;
import model.BillItem;
import model.BillItemDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillDAO {

    public int addBill(Bill bill) throws Exception {
        int generatedBillId = -1;
        String insertBillSQL = "INSERT INTO bills (accountNo, bill_date) VALUES (?, ?)";
        String insertItemSQL = "INSERT INTO bill_items (bill_id, productNo, quantity) VALUES (?, ?, ?)";
        String checkCustomerSQL = "SELECT COUNT(*) FROM customers WHERE accountNo = ?";
        String checkProductSQL = "SELECT unit FROM products WHERE productNo = ?";
        String updateProductQtySQL = "UPDATE products SET unit = unit - ? WHERE productNo = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Validate customer exists
            try (PreparedStatement psCheckCustomer = conn.prepareStatement(checkCustomerSQL)) {
                psCheckCustomer.setInt(1, bill.getAccountNo());
                try (ResultSet rs = psCheckCustomer.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        if (count == 0) {
                            throw new Exception("Customer with accountNo " + bill.getAccountNo() + " does not exist.");
                        }
                    }
                }
            }

            // 2. Validate product existence and stock availability
            for (BillItem item : bill.getItems()) {
                try (PreparedStatement psCheckProduct = conn.prepareStatement(checkProductSQL)) {
                    psCheckProduct.setInt(1, item.getProductNo());
                    try (ResultSet rs = psCheckProduct.executeQuery()) {
                        if (rs.next()) {
                            int stockQty = rs.getInt("unit");
                            if (stockQty < item.getQuantity()) {
                                throw new Exception("Not enough stock for productNo " + item.getProductNo() +
                                        ". Available: " + stockQty + ", requested: " + item.getQuantity());
                            }
                        } else {
                            throw new Exception("Product with productNo " + item.getProductNo() + " does not exist.");
                        }
                    }
                }
            }

            // 3. Insert bill
            try (PreparedStatement psBill = conn.prepareStatement(insertBillSQL, Statement.RETURN_GENERATED_KEYS)) {
                psBill.setInt(1, bill.getAccountNo());
                psBill.setTimestamp(2, new Timestamp(bill.getBillDate().getTime()));
                int affectedRows = psBill.executeUpdate();

                if (affectedRows == 0) {
                    conn.rollback();
                    throw new Exception("Creating bill failed, no rows affected.");
                }

                try (ResultSet generatedKeys = psBill.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedBillId = generatedKeys.getInt(1);
                    } else {
                        conn.rollback();
                        throw new Exception("Creating bill failed, no ID obtained.");
                    }
                }
            }

            // 4. Insert bill items and update product quantities
            try (PreparedStatement psItem = conn.prepareStatement(insertItemSQL);
                 PreparedStatement psUpdateQty = conn.prepareStatement(updateProductQtySQL)) {

                for (BillItem item : bill.getItems()) {
                    // Insert bill item
                    psItem.setInt(1, generatedBillId);
                    psItem.setInt(2, item.getProductNo());
                    psItem.setInt(3, item.getQuantity());
                    psItem.addBatch();

                    // Update product quantity
                    psUpdateQty.setInt(1, item.getQuantity());
                    psUpdateQty.setInt(2, item.getProductNo());
                    psUpdateQty.addBatch();
                }

                psItem.executeBatch();
                psUpdateQty.executeBatch();
            }

            conn.commit();
        } catch (Exception e) {
            throw e; // Let the servlet catch and display this
        }
        return generatedBillId;
    }



    // Get bill by ID (optional, can be implemented later)
    public Bill getBill(int billId) {
        Bill bill = null;
        String selectBillSQL = "SELECT * FROM bills WHERE bill_id = ?";
        String selectItemsSQL = "SELECT * FROM bill_items WHERE bill_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psBill = conn.prepareStatement(selectBillSQL);
             PreparedStatement psItems = conn.prepareStatement(selectItemsSQL)) {

            psBill.setInt(1, billId);
            ResultSet rsBill = psBill.executeQuery();

            if (rsBill.next()) {
                int accountNo = rsBill.getInt("accountNo");
                Date billDate = new Date(rsBill.getTimestamp("bill_date").getTime());

                psItems.setInt(1, billId);
                ResultSet rsItems = psItems.executeQuery();
                List<BillItem> items = new ArrayList<>();

                while (rsItems.next()) {
                    BillItem item = new BillItem(
                            rsItems.getInt("item_id"),
                            billId,
                            rsItems.getInt("productNo"),
                            rsItems.getInt("quantity")
                    );
                    items.add(item);
                }

                bill = new Bill(billId, accountNo, billDate, items);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bill;
    }

    public List<Bill> getAllBills() throws SQLException {
        List<Bill> billList = new ArrayList<>();

        String sql = "SELECT * FROM bills";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Bill bill = new Bill();
                bill.setBillId(rs.getInt("bill_id"));
                bill.setAccountNo(rs.getInt("accountNo"));
                bill.setBillDate(rs.getTimestamp("bill_date"));
                billList.add(bill);
            }
        }

        return billList;
    }

    public Bill getBillById(int billId) throws SQLException {
        Bill bill = null;

        String sql = "SELECT b.bill_id, b.account_no, b.bill_date, c.name AS customer_name, " +
                "b.total_amount " +
                "FROM bills b " +
                "JOIN customers c ON b.account_no = c.account_no " +
                "WHERE b.bill_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, billId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setAccountNo(rs.getInt("account_no"));
                    bill.setBillDate(rs.getDate("bill_date"));
                    bill.setCustomerName(rs.getString("customer_name"));
                    bill.setTotalAmount(rs.getDouble("total_amount"));

                    // Fetching the items is handled separately in BillItemDAO
                }
            }
        }

        return bill;
    }


}
