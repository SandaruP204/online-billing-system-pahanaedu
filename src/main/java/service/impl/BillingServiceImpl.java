package service.impl;

import dao.DBConnection;
import service.BillingService;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

/** Pure JDBC. One transaction. Conditional UPDATE prevents negative stock. */
public class BillingServiceImpl implements BillingService {

    @Override
    public int createBill(int accountNo, List<BillingService.Line> lines)
            throws BillingService.InsufficientStockException, Exception {

        if (lines == null || lines.isEmpty()) throw new IllegalArgumentException("No items.");
        for (BillingService.Line l : lines) {
            if (l.quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        }

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                // 1) Validate customer + fetch name
                String customerName = null;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT name FROM customers WHERE accountNo=?")) {
                    ps.setInt(1, accountNo);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) customerName = rs.getString(1);
                    }
                }
                if (customerName == null || customerName.isBlank()) {
                    throw new IllegalArgumentException("Invalid account number: " + accountNo);
                }

                // 2) Fetch current prices
                Map<Integer, BigDecimal> price = new HashMap<>();
                String placeholders = String.join(",", Collections.nCopies(lines.size(), "?"));
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT productNo, price FROM products WHERE productNo IN (" + placeholders + ")")) {
                    int i = 1;
                    for (BillingService.Line l : lines) ps.setInt(i++, l.productNo);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) price.put(rs.getInt(1), rs.getBigDecimal(2));
                    }
                }
                for (BillingService.Line l : lines) {
                    if (!price.containsKey(l.productNo)) {
                        throw new IllegalArgumentException("Unknown product: " + l.productNo);
                    }
                }

                // 3) Decrement stock conditionally + compute total
                BigDecimal total = BigDecimal.ZERO;
                try (PreparedStatement dec = con.prepareStatement(
                        "UPDATE products SET unit = unit - ? WHERE productNo = ? AND unit >= ?")) {
                    for (BillingService.Line l : lines) {
                        dec.setInt(1, l.quantity);
                        dec.setInt(2, l.productNo);
                        dec.setInt(3, l.quantity);
                        int updated = dec.executeUpdate();
                        if (updated == 0) {
                            int avail = 0;
                            try (PreparedStatement ps = con.prepareStatement(
                                    "SELECT unit FROM products WHERE productNo=?")) {
                                ps.setInt(1, l.productNo);
                                try (ResultSet rs = ps.executeQuery()) {
                                    if (rs.next()) avail = rs.getInt(1);
                                }
                            }
                            throw new BillingService.InsufficientStockException(
                                    l.productNo, l.quantity, avail);
                        }
                        total = total.add(price.get(l.productNo)
                                .multiply(BigDecimal.valueOf(l.quantity)));
                    }
                }

                // 4) Insert bill
                int billId;
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO bills (accountNo, customer_name, bill_date, total_amount) " +
                                "VALUES (?,?,NOW(),?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, accountNo);
                    ps.setString(2, customerName);
                    ps.setBigDecimal(3, total);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        billId = keys.getInt(1);
                    }
                }

                // 5) Insert bill items
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO bill_items (bill_id, productNo, quantity) VALUES (?,?,?)")) {
                    for (BillingService.Line l : lines) {
                        ps.setInt(1, billId);
                        ps.setInt(2, l.productNo);
                        ps.setInt(3, l.quantity);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                con.commit();
                return billId;

            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
}
