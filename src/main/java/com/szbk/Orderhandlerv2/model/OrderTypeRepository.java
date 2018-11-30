package com.szbk.Orderhandlerv2.model;

import com.szbk.Orderhandlerv2.model.Entity.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderTypeRepository extends JpaRepository<OrderType, Long> {
    @Query(value = "select name from OrderType")
    List<String> getTypeNames();
}
