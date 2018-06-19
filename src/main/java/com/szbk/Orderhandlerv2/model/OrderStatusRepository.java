package com.szbk.Orderhandlerv2.model;

import java.util.List;

import com.szbk.Orderhandlerv2.model.Entity.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    //TODO Status names are needed and not the entire status object.
    @Query(value = "select name from OrderStatus")
    List<String> getAllStatusNames();
}