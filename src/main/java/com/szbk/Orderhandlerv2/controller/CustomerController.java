package com.szbk.Orderhandlerv2.controller;

import com.szbk.Orderhandlerv2.model.CustomerRepository;
import com.szbk.Orderhandlerv2.model.Entity.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerController {
    private CustomerRepository repo;

    public CustomerController(CustomerRepository repo) {
        this.repo = repo;
    }

    public Customer getCustomerByEmail(String emailAddress) {
        return repo.findCustomerByEmail(emailAddress);
    }

    public Customer getCustomerById(long id) {
        return repo.getOne(id);
    }

    public List<Customer> getCustomersByIds(ArrayList<Long> ids) {
        return repo.findAllById(ids);
    }

    public Customer getCustomerByCompanyNameAndGroupNameAndCustomerName(String companyName, String groupName, String customerName) {
        return repo.findCustomerByCompanyNameAndGroupNameAndCustomerName(companyName, groupName, customerName);
    }

    public List<String> getCompanyNames() {
        return repo.getCompanyNames();
    }

    public List<String> getGroupNamesByCompanyName(String companyName) {
        return repo.getGroupNamesByCompanyName(companyName);
    }

    public List<String> getCustomerNamesByCompanyNameAndGroupName(String companyName, String groupName) {
        return repo.getCustomerNamesByCompanyNameAndGroupName(companyName, groupName);
    }

    public ArrayList<String> getAllCustomersNameAndEmail() {
        ArrayList<String> customerNameAndEmail = new ArrayList<>();
        List<Customer> customers = repo.findAll();
        String value;

        System.out.println("controller: " + customers);
        for (Customer c : customers) {
            value = c.getId() + ": " + c.getCustomerName() + " (" + c.getEmail() + ")";
//            System.out.println("value: " + value);
            customerNameAndEmail.add(value);
        }

//        System.out.println("controller: " + customers);

        return customerNameAndEmail;
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

        System.out.println("innerName: " + customerInnerName.toString());

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
