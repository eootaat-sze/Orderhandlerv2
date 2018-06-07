package com.szbk.Orderhandlerv2.model;

import com.szbk.Orderhandlerv2.model.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public Customer findCustomerByEmail(String email);

    public Customer findCustomerByEmailAndPassword(String email, String password);

//    @Query("SELECT customerName, email FROM Customer")
//    public List<Customer> getCustomersNameAndEmail();
}
