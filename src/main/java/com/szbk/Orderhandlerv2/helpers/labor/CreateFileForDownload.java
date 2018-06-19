package com.szbk.Orderhandlerv2.helpers.labor;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Set;

import com.szbk.Orderhandlerv2.controller.CustomerController;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.server.StreamResource;

public class CreateFileForDownload {
    private CustomerController customerController;
    private ArrayList<CustomerOrder> ordersToExport;
    private String customerNameAsOwner;

    public CreateFileForDownload(CustomerController customerController) {
        this.customerController = customerController;

        this.customerNameAsOwner = "";
        ordersToExport = new ArrayList<>();
    }

    public StreamResource createResourceForDownload() {
        StringBuilder builder = new StringBuilder("Szekvencia,Scale,Típus,Tisztítás\n");

        for (CustomerOrder item : ordersToExport) {
            builder.append(item.getEditedSequence());
            builder.append(",");
            builder.append(item.getScale());
            builder.append(",");
            builder.append(item.getType());
            builder.append(",");
            builder.append(item.getPurification());
            builder.append("\n");
        }

        final StreamResource resource = new StreamResource(
            () -> new ByteArrayInputStream(builder.toString().getBytes()), createFileName());

        return resource;
    }

    private String createFileName() {
        // String customerName = customerController.getCustomerById(ordersToExport.get(0).getCustomerId()).getCustomerName();
        String filename = customerNameAsOwner.trim().replace(" ", "-").concat("_report.csv");
        System.out.println("filename: " + filename);
        return filename;
	}

	public ArrayList<CustomerOrder> getOrdersToExport() {
        return ordersToExport;
    }

    public void setOrdersToExport(Set<CustomerOrder> orders) {
        ordersToExport.addAll(orders);
    }

    public void setCustomerNameAsOwner(String customerName) {
        this.customerNameAsOwner = customerName;
    }
}