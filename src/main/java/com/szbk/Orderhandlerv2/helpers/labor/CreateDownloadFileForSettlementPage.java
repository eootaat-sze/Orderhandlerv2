package com.szbk.Orderhandlerv2.helpers.labor;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import com.szbk.Orderhandlerv2.controller.CustomerController;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.server.StreamResource;

public class CreateDownloadFileForSettlementPage {
    private CustomerController customerController;
    private ArrayList<CustomerOrder> ordersToExport;
    private String[] selectedNames;
    private String customerNameAsOwner;

    public CreateDownloadFileForSettlementPage(CustomerController customerController, String... names) {
        this.customerController = customerController;
        this.selectedNames = names;

        this.customerNameAsOwner = "";
        ordersToExport = new ArrayList<>();
    }

    public StreamResource createResourceForDownload() {
        StringBuilder builder = new StringBuilder("Szekvencia neve,Szekvencia,Megrendelés ára\n");

        for (CustomerOrder item : ordersToExport) {
            builder.append(item.getSequenceName());
            builder.append(",");
            builder.append(item.getEditedSequence());
            builder.append(",");
            builder.append(item.getPrice());
            builder.append("\n");
        }

        final StreamResource resource = new StreamResource(
            () -> new ByteArrayInputStream(builder.toString().getBytes()), createFileName());

        return resource;
    }

    private String createFileName() {
        // String customerName = customerController.getCustomerById(ordersToExport.get(0).getCustomerId()).getCustomerName();
        StringBuilder builder = new StringBuilder();
        for (String name: selectedNames) {
            name = name.trim();
            builder.append(name.replace(" ", "-"));
            builder.append("_");
        }

        builder.append("szamlazas.csv");

        System.out.println("filename: " + builder.toString());
        return builder.toString();
	}

	public ArrayList<CustomerOrder> getOrdersToExport() {
        return ordersToExport;
    }

    public void setOrdersToExport(List<CustomerOrder> orders) {
        ordersToExport.addAll(orders);
    }

    public void setCustomerNameAsOwner(String customerName) {
        this.customerNameAsOwner = customerName;
    }
    
}