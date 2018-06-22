package com.szbk.Orderhandlerv2.controller;

import java.util.List;

import com.szbk.Orderhandlerv2.model.OrderStatusRepository;
import com.szbk.Orderhandlerv2.model.Entity.OrderStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusController {
    private OrderStatusRepository repo;

    public OrderStatusController(OrderStatusRepository repository) {
        this.repo = repository;
    }

    public List<String> getAllStatusNames() {
        List<String> allStatusNames = repo.getAllStatusNames();
        System.out.println("status: " + allStatusNames);
        System.out.println("repo: " + repo);

        return allStatusNames;
    }

    public List<OrderStatus> getAllStatus() {
        return repo.findAll();
    }
}