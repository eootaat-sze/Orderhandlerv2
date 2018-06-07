package com.szbk.Orderhandlerv2.controller;

import com.szbk.Orderhandlerv2.model.CustomerOrderRepository;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderController {
    @Autowired
    CustomerOrderRepository repo;

    public List<CustomerOrder> getAllOrdersByCustomerId(long customerId) {
        return repo.findCustomerOrderByCustomerId(customerId);
    }

    public boolean saveOrder(CustomerOrder order) {

        long count = repo.count();

        repo.save(order);
        System.out.println("Order saved");

        return repo.count() > count;
    }

    public boolean saveManyOrder(ArrayList<CustomerOrder> ordersToSave) {
        long count = repo.count();
        boolean success = false;

        for (CustomerOrder order : ordersToSave) {
            success = saveOrder(order);

            if (!success) {
                System.out.println("Something went wrong during db insert!");
                break;
            }
        }

        return success;
    }

    public List<CustomerOrder> getAllOrders() {
        return repo.findAll();
    }
}
