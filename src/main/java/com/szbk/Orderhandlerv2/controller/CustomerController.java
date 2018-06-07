package com.szbk.Orderhandlerv2.controller;

import com.szbk.Orderhandlerv2.model.CustomerRepository;
import com.szbk.Orderhandlerv2.model.Entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerController {
    private CustomerRepository repo;

    public CustomerController(CustomerRepository repo) {
        this.repo = repo;
    }

    public boolean registration(Customer customer) {
        createInnerNameToCustomer(customer);
        //Save the number of rows in the database.
        long count = repo.count();

        System.out.println("Customer to save: " + customer);
        repo.save(customer);

        //If the current row number is bigger than the previous, the insert was successful.
        return repo.count() > count;
    }

    private void createInnerNameToCustomer(Customer customer) {
        String customerName = customer.getCustomerName();
        String[] splitName = customerName.split(" ");
        StringBuilder customerInnerName = new StringBuilder();

        for (String s : splitName) {
            customerInnerName.append(s.charAt(0));
        }

        customer.setInnerName(customerInnerName.toString());
    }

    public Customer login(String email, String password) {
        Customer loginCustomer = repo.findCustomerByEmailAndPassword(email, password);

        System.out.println("Customer to login: " + loginCustomer);
        return loginCustomer;
    }

//    public List<String> getAllCustomersNameAndEmail() {
//        List<String> customersNameAndEmail = new ArrayList<>();
//        List<Customer> namesAndEmails = repo.getCustomersNameAndEmail();
//        String value;
//
//        for (Customer c: namesAndEmails) {
//            value = c.getEmail() + " - " + c.getCustomerName();
//            customersNameAndEmail.add(value);
//        }
//
//        return customersNameAndEmail;
//    }

//    public Customer findCustomerByEmail(String email) {
//        return repo.findCustomerByEmail(email);
//    }

    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }
}
