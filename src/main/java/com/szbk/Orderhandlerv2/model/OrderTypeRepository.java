package com.szbk.Orderhandlerv2.model;

import com.szbk.Orderhandlerv2.model.Entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderTypeRepository extends JpaRepository<Type, Long> {
    @Query(value = "select name from Type")
    List<String> getTypeNames();
}
