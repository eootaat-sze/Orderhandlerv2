package com.szbk.Orderhandlerv2.controller;

import com.szbk.Orderhandlerv2.model.Entity.OrderType;
import com.szbk.Orderhandlerv2.model.OrderTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderTypeController {
    @Autowired
    OrderTypeRepository repo;

    public boolean saveType(OrderType t) {
        long count = repo.count();
        repo.save(t);

        return repo.count() > count;
    }

    public List<String> getTypeNames() {
        List<String> typeNames = repo.getTypeNames();
        System.out.println("typeNames: " + typeNames);

        return typeNames;
    }

    public List<String> getTypeNamesAndPricesAsStrings() {
        List<OrderType> typesList = repo.findAll();
        List<String> values = new ArrayList<>();
        String value;

        for (OrderType item : typesList) {
            value = item.getName() + " - " + item.getPrice() + " Ft";
            values.add(value);
        }

        return values;
    }

    public List<OrderType> getAllTypes() {
        return repo.findAll();
    }
}
