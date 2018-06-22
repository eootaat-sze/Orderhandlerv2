package com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents;

import com.szbk.Orderhandlerv2.controller.CustomerController;
import com.szbk.Orderhandlerv2.model.Entity.Customer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;

public class CustomerComponent extends HorizontalLayout {
    private CustomerController customerController;
    private Grid<Customer> customerContentGrid;

    public CustomerComponent(CustomerController controller) {
        customerController = controller;
        customerContentGrid = new Grid<>(Customer.class);

        setupContent();
    }

	private void setupContent() {
        setSizeFull();
        addComponents(customerContentGrid);
        setExpandRatio(customerContentGrid, 1);

        //Content grid settings
        customerContentGrid.setSizeFull();
        customerContentGrid.setColumnOrder("customerName", "email", "password", "companyName", "groupName", "innerName", "id");
        customerContentGrid.getColumn("customerName").setCaption("Megrendelő neve");
        customerContentGrid.getColumn("email").setCaption("Email címe");
        customerContentGrid.getColumn("password").setCaption("Jelszava");
        customerContentGrid.getColumn("companyName").setCaption("Cég neve");
        customerContentGrid.getColumn("groupName").setCaption("Csoport neve");
        customerContentGrid.getColumn("innerName").setCaption("Belső neve");
        // statusContentGrid.asSingleSelect().addValueChangeListener(event -> selectRow(event));
        updateGrid();
	}

	private void updateGrid() {
        customerContentGrid.setItems(customerController.getAllCustomers());
	}
}