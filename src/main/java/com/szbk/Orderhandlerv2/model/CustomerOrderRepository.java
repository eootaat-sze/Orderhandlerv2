package com.szbk.Orderhandlerv2.model;

import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findCustomerOrderByCustomerId(long customerID);

    List<CustomerOrder> findCustomerOrderByCustomerIdAndStatusEquals(long customerId, String status);

    List<CustomerOrder> findCustomerOrdersByStatus(String status);

    //These are for the step-by-step filtering in the reportpage.
//    @Query("select distinct companyName from CustomerOrder")
//    List<String> getAllCompanyNames();
//
//    @Query("select distinct groupName from CustomerOrder where companyName = ?")
//    List<String> getAllGroupNames(String companyName);
//
//    @Query("select distinct customerName from CustomerOrder where companyName = ?1 and groupName = ?2")
//    List<String> getAllCustomerNames(String companyName, String groupName);
}
