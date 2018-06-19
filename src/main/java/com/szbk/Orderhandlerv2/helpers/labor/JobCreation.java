package com.szbk.Orderhandlerv2.helpers.labor;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.szbk.Orderhandlerv2.controller.CustomerController;
import com.szbk.Orderhandlerv2.controller.OrderController;
import com.szbk.Orderhandlerv2.model.Entity.Customer;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.server.StreamResource;

public class JobCreation {
    private OrderController orderController;
    private CustomerController customerController;
    
    private ArrayList<CustomerOrder> ordersForJob;

    public JobCreation() {}

    public JobCreation(CustomerController customerController) {
        this.customerController = customerController;

        ordersForJob = new ArrayList<>();
    }

    public StreamResource createResourceForDownload() {
        //Create an inner id for the orders, if they already don't have one.
        createInnerIdForOrders();
        StringBuilder builder = new StringBuilder();
        builder.append("\"Applied Biosystems Oligonucleotide Synthesizer Software Synthesis File\"\n");

        for (CustomerOrder order : ordersForJob) {
            // builder.append("\"Oligo ID: " + ((CustomerOrder) order).getId() + "\"\n");
            // builder.append("\"Number of Bases: " + ((CustomerOrder) order).getSequence().length() + "\"\n");
            // builder.append("\"Sequence: " + ((CustomerOrder) order).getSequence() + "\"\n");
            // builder.append("\"Vial ID: " + ((CustomerOrder) order).getCustomerName() + "!#!belnév#!#"
            //         + ((CustomerOrder) order).getPurification() + "\"\n");
            // builder.append("\n");
            builder.append("\"Oligo ID: " + order.getId() + "\"\n");
            builder.append("\"Number of Bases: " + order.getSequence().length() + "\"\n");
            builder.append("\"Sequence: " + order.getSequence() + "\"\n");
            //What should I exactly write here? I mean I can get the customer name if it's needed here, no problem.
            // builder.append("\"Vial ID: " + order.getCustomerName() + "!#!belnév#!#"
            //         + ((CustomerOrder) order).getPurification() + "\"\n");
        }

        System.out.println("file: " + builder.toString());

        final StreamResource resource = new StreamResource(
            () -> new ByteArrayInputStream(builder.toString().getBytes()),
            createFileName()
        );

        return resource;
    }

    //Create a filename in the following format: {year}+{month}+{day}-{hours}+{minutes}.syn
    private String createFileName() {
        LocalDateTime dateTime = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(dateTime.getYear()));

        if (dateTime.getMonthValue() < 10) {
            sb.append("0");
        }

        sb.append(String.valueOf(dateTime.getMonthValue()));
        sb.append(String.valueOf(dateTime.getDayOfMonth()));
        sb.append("-");
        sb.append(String.valueOf(dateTime.getHour()));

        //For whatever reason it is a dash (-) in the filename.
        sb.append(":");

        if (dateTime.getMinute() < 10) {
            sb.append("0");
        }

        sb.append(String.valueOf(dateTime.getMinute()));
        sb.append(".syn");

        System.out.println("filename: " + sb.toString());
        return sb.toString();
    }

    private void createInnerIdForOrders() {
        ArrayList<Long> customerIds = new ArrayList<>();

        for (CustomerOrder order: ordersForJob) {
            if (order.getCustomerInnerId() == null) {
                customerIds.add(order.getCustomerId());
                // Customer c = customerController.getCustomerById(order.getCustomerId());
                // String innerId = c.getInnerName() + order.getId();
                // order.setCustomerInnerId(innerId);
                // System.out.println("order with inner id: " + order);
            }
        }

        List<Customer> customersForOrders = customerController.getCustomersByIds(customerIds);

        for (int i = 0; i < ordersForJob.size(); i++) {
            for (int j = 0; j < customersForOrders.size(); j++) {
                CustomerOrder o = ordersForJob.get(i);
                Customer c = customersForOrders.get(j);

                if (o.getCustomerId() == c.getId()) {
                    o.setCustomerInnerId(c.getInnerName() + o.getId());
                }
            }
        }

        ordersForJob.forEach(item -> System.out.println("item: " + item));
    }

    public ArrayList<CustomerOrder> getOrdersForJob() {
        return ordersForJob;
    }

    public void setOrdersForJob(Set<CustomerOrder> ordersForJob) {
        //Actually, convert the given set to a list.
        this.ordersForJob.addAll(ordersForJob);
    }
}