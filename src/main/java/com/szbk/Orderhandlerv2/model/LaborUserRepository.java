package com.szbk.Orderhandlerv2.model;

import com.szbk.Orderhandlerv2.model.Entity.LaborUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaborUserRepository extends JpaRepository<LaborUser, Long> {
    public LaborUser findLaborUserByEmailAndPassword(String email, String password);
}
