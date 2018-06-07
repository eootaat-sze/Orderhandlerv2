package com.szbk.Orderhandlerv2.model;

import com.szbk.Orderhandlerv2.model.Entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTypeRepository extends JpaRepository<Type, Long> {
}
