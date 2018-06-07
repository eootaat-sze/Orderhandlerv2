package com.szbk.Orderhandlerv2.controller;

import com.szbk.Orderhandlerv2.model.Entity.LaborUser;
import com.szbk.Orderhandlerv2.model.LaborUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaborUserController {
    @Autowired
    LaborUserRepository repo;

    public boolean registration(LaborUser lb) {
        long count = repo.count();

        repo.save(lb);

        return repo.count() > count;
    }

    public LaborUser login(String email, String password) {
        LaborUser loginLaborUser = repo.findLaborUserByEmailAndPassword(email, password);

        System.out.println("Laboruser to login: " + loginLaborUser);
        return loginLaborUser;
    }

    public List<LaborUser> getAllLaborUsers() {
        return repo.findAll();
    }
}
