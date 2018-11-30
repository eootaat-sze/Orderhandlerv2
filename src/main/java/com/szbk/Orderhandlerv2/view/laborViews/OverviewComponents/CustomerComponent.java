package com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents;

import com.szbk.Orderhandlerv2.controller.CustomerController;
import com.szbk.Orderhandlerv2.model.Entity.Customer;
import com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents.Forms.CustomerActionForm;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CustomerComponent extends HorizontalLayout {
    private CustomerController customerController;
    private Grid<Customer> customerContentGrid;
    private VerticalLayout actionLayout;
    private Button addItemBtn;
    private CustomerActionForm actionForm;

    public CustomerComponent(CustomerController controller) {
        customerController = controller;
        customerContentGrid = new Grid<>(Customer.class);
        actionForm = new CustomerActionForm(this, customerController);
        addItemBtn = new Button("Új elem hozzáadása");
        actionLayout = new VerticalLayout(actionForm, addItemBtn);

        setupContent();
    }

    @Override
    public void attach() {
        super.attach();
        setupContent();

        System.out.println("setupcontent[customercomponent] called");
    }

	private void setupContent() {
        setSizeFull();
        addComponents(customerContentGrid, actionLayout);
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
        customerContentGrid.asSingleSelect().addValueChangeListener(event -> selectRow(event));
        updateGrid();

        //Action layout settings
        actionLayout.setWidth(300, Unit.PIXELS);
        actionLayout.setHeight(100, Unit.PERCENTAGE);
        actionLayout.setMargin(false);
        actionLayout.setComponentAlignment(addItemBtn, Alignment.BOTTOM_CENTER);

        //Action from settings
        actionForm.setVisible(false);

        //Add item btn settings
        addItemBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        addItemBtn.addClickListener(event -> newItemClick());
    }
    
    private void selectRow(ValueChangeEvent<Customer> event) {
		if (event.getValue() == null) {
            actionForm.setVisible(false);
        } else {
            actionForm.setCustomerToEdit(event.getValue());
        }
	}

	private void newItemClick() {
        customerContentGrid.asSingleSelect().clear();
        actionForm.setCustomerToEdit(new Customer());
    }

	public void updateGrid() {
        customerContentGrid.setItems(customerController.getAllCustomers());
	}
}