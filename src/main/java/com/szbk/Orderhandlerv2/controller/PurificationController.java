package com.szbk.Orderhandlerv2.controller;

import com.szbk.Orderhandlerv2.model.Entity.Purification;
import com.szbk.Orderhandlerv2.model.PurificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurificationController {
    @Autowired
    PurificationRepository repo;

    public boolean savePurification(Purification p) {
        long count = repo.count();
        repo.save(p);

        return repo.count() > count;
    }

    public List<String> getPurificationNamesAndPricesAsStrings() {
        List<Purification> purificationList = repo.findAll();
        List<String> namesAndPrices = new ArrayList<>();
        String value;

        for (Purification item : purificationList) {
            value = item.getName() + " - " + item.getPrice() + " Ft";
            namesAndPrices.add(value);
        }

        return namesAndPrices;
    }

    public List<Purification> getAllPurifications() {
        return repo.findAll();
    }
}
