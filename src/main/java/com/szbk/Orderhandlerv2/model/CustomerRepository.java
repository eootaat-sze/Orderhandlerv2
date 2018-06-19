package com.szbk.Orderhandlerv2.model;

import java.util.List;

import com.szbk.Orderhandlerv2.model.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public Customer findCustomerByEmail(String email);

    // public Customer findCustomerById(long customerId);

    public Customer findCustomerByEmailAndPassword(String email, String password);

    public Customer findCustomerByCompanyNameAndGroupNameAndCustomerName(String companyName, String groupName, String customerName);

    @Query(value = "select distinct companyName from Customer")
    public List<String> getCompanyNames();

    @Query(value = "select distinct groupName from Customer where companyName = ?1")
    public List<String> getGroupNamesByCompanyName(String companyName);

    @Query(value = "select distinct customerName from Customer where companyName = ?1 and groupName = ?2")
    public List<String> getCustomerNamesByCompanyNameAndGroupName(String companyName, String groupName);

//    @Query("SELECT customerName, email FROM Customer")
//    public List<Customer> getCustomersNameAndEmail();
}
