package com.szbk.Orderhandlerv2.controller;

import com.szbk.Orderhandlerv2.model.CustomerOrderRepository;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.server.VaadinSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class OrderController {
    @Autowired
    CustomerOrderRepository repo;

    public List<CustomerOrder> getAllOrdersByCustomerId(long customerId) {
        // System.out.println("get all orders");
        return repo.findCustomerOrderByCustomerId(customerId);
    }

    public List<CustomerOrder> getOrdersByStatus(String status) {
        // return repo.
        return repo.findCustomerOrdersByStatus(status);
    }

    public boolean saveOrder(CustomerOrder order) {

        long count = repo.count();

        //I set the edited sequence (if it's empty) to the value of the sequence and it can be editable on the
        //labor side, but the original sequence, which was given by the customer won't be lost.
        if (order.getEditedSequence() == null) {
            order.setEditedSequence(order.getSequence());
            System.out.println("edited seq order: " + order);
        }

        repo.save(order);
        System.out.println("Order saved");

        return repo.count() > count;
    }

    public List<CustomerOrder> filterOrdersByCustomerIdAndStatus(long customerId, String status) {
        return repo.findCustomerOrderByCustomerIdAndStatusEquals(customerId, status);
    }

    //After the job creation, the orders need a status change. That's what this method does. And saves them as well.
    public void changeStatusOnOrders(Set<CustomerOrder> orders, String status) {
        for (CustomerOrder order: orders) {
            order.setStatus(status);
            saveOrder(order);
        }
    }

    public void changeStatusOnOrders(List<CustomerOrder> orders, String status) {
        for (CustomerOrder order: orders) {
            order.setStatus(status);
            saveOrder(order);
        }
    }

    public boolean saveManyOrder(ArrayList<CustomerOrder> ordersToSave) {
        boolean success = false;

        for (CustomerOrder order : ordersToSave) {
            System.out.println("Order to save: " + order);
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

    public List<CustomerOrder> processUploadedFileAsString(String content) {
        String[] splittedContent = content.split("\n");
        String[] row;
        List<CustomerOrder> uploadedOrders = new ArrayList<>();
        //type, seq.name, seq, scale, purification
        //seq.name, seq, scale, purification, type, order_date
        // for (String s: splittedContent) {
        for (int i = 1; i < splittedContent.length; i++) {
            row = splittedContent[i].split(";");
            System.out.println("row: " + splittedContent[i]);
            System.out.println("row[3].length: " + row[3].trim().length());
            uploadedOrders.add(new CustomerOrder(row[0], row[1], Integer.parseInt(row[2].trim()), row[3], "DNS", LocalDate.now()));
        }

        return uploadedOrders;
    }

	public List<CustomerOrder> setCustomerNameAndIdToOrders(List<CustomerOrder> uploadedOrders, VaadinSession session) {
		uploadedOrders.forEach(item -> {
            item.setCustomerId(Long.parseLong(String.valueOf(session.getAttribute("id"))));
            item.setCustomerName(String.valueOf(session.getAttribute("customerName")));
        });

        return uploadedOrders;
	}
}
