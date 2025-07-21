package dao;

import model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDAO {

    public void addCustomer(Customer customer) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO customers (accountNo, name, address, phone, unitsConsumed) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customer.getAccountNo());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getPhone());
            ps.setInt(5, customer.getUnitsConsumed());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomer(int accountNo) {
        Customer customer = null;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM customers WHERE accountNo = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, accountNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                customer = new Customer();
                customer.setAccountNo(rs.getInt("accountNo"));
                customer.setName(rs.getString("name"));
                customer.setAddress(rs.getString("address"));
                customer.setPhone(rs.getString("phone"));
                customer.setUnitsConsumed(rs.getInt("unitsConsumed"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }
}
