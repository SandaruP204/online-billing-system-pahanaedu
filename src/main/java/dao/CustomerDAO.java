package dao;

import model.Customer;
import java.util.List;

public interface CustomerDAO {
    void addCustomer(Customer customer);
    List<Customer> getAllCustomers();
    Customer getCustomer(int accountNo);
    void deleteCustomer(int accountNo);
    void updateCustomer(Customer customer);
}
