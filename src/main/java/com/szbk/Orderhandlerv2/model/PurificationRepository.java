package com.szbk.Orderhandlerv2.model;

import com.szbk.Orderhandlerv2.model.Entity.Purification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurificationRepository extends JpaRepository<Purification, Long> {
    @Query(value = "select name from Purification ")
    List<String> getPurificationNames();
}
