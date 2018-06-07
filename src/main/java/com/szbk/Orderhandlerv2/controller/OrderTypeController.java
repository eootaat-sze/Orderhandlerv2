package com.szbk.Orderhandlerv2.controller;

import com.szbk.Orderhandlerv2.model.OrderTypeRepository;
import com.szbk.Orderhandlerv2.model.Entity.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderTypeController {
    @Autowired
    OrderTypeRepository repo;

    public boolean saveType(Type t) {
        long count = repo.count();
        repo.save(t);

        return repo.count() > count;
    }

    public List<String> getTypeNamesAndPricesAsStrings() {
        List<Type> typesList = repo.findAll();
        List<String> values = new ArrayList<>();
        String value;

        for (Type item : typesList) {
            value = item.getName() + " - " + item.getPrice() + " Ft";
            values.add(value);
        }

        return values;
    }

    public List<Type> getAllTypes() {
        return repo.findAll();
    }
}
