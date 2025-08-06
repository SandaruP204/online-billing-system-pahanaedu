package dao;

import model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

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

    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM customers";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setAccountNo(rs.getInt("accountNo"));
                customer.setName(rs.getString("name"));
                customer.setAddress(rs.getString("address"));
                customer.setPhone(rs.getString("phone"));
                customer.setUnitsConsumed(rs.getInt("unitsConsumed"));
                customerList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;
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


    public void deleteCustomer(int accountNo) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "DELETE FROM customers WHERE accountNo = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, accountNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateCustomer(Customer customer) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE customers SET name = ?, address = ?, phone = ?, unitsConsumed = ? WHERE accountNo = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPhone());
            ps.setInt(4, customer.getUnitsConsumed());
            ps.setInt(5, customer.getAccountNo());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
