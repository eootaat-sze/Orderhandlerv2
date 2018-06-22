package com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents;

import com.szbk.Orderhandlerv2.controller.OrderController;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CustomerOrderComponent extends HorizontalLayout {
    private OrderController orderController;
    private Grid<CustomerOrder> orderContentGrid;
    private VerticalLayout actionLayout;
    private Button addItemBtn;

    public CustomerOrderComponent(OrderController controller) {
        orderController = controller;
        orderContentGrid = new Grid<>(CustomerOrder.class);
        addItemBtn = new Button("Új elem hozzáadása");
        actionLayout = new VerticalLayout(addItemBtn);

        setupContent();
    }

    private void setupContent() {
        //View settings
        setSizeFull();
        addComponents(orderContentGrid);
        setExpandRatio(orderContentGrid, 1);

        //Content grid settings
        orderContentGrid.setSizeFull();
        orderContentGrid.setColumnOrder("sequenceName", "sequence", "editedSequence", "type", "purification", "scale", "price", "status", "orderDate", "customerId", "customerInnerId", "id");
        orderContentGrid.getColumn("sequenceName").setCaption("Szekvencia neve");
        orderContentGrid.getColumn("sequenceName").setMaximumWidth(300);
        orderContentGrid.getColumn("sequence").setCaption("Szekvencia");
        orderContentGrid.getColumn("sequence").setMaximumWidth(300);
        orderContentGrid.getColumn("editedSequence").setCaption("Szekvencia (sz)");
        orderContentGrid.getColumn("editedSequence").setMaximumWidth(300);
        orderContentGrid.getColumn("type").setCaption("Típus");
        orderContentGrid.getColumn("purification").setCaption("Tisztítás");
        orderContentGrid.getColumn("price").setCaption("Ár (Forint)");
        orderContentGrid.getColumn("status").setCaption("Státusz");
        orderContentGrid.getColumn("orderDate").setCaption("Megrendelés dátuma");
        orderContentGrid.getColumn("customerId").setCaption("Megrendelő azonosító");
        orderContentGrid.getColumn("customerInnerId").setCaption("Belső azonosító");
        // orderContentGrid.asSingleSelect().addValueChangeListener(event -> selectRow(event));
        updateGrid();

        //Action layout settings
        actionLayout.setSizeUndefined();
        actionLayout.setComponentAlignment(addItemBtn, Alignment.TOP_CENTER);

        //Add item btn settings
        addItemBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        addItemBtn.addClickListener(event -> newItemClick());
    }

	public void updateGrid() {
        orderContentGrid.setItems(orderController.getAllOrders());
	}

	private void newItemClick() {
        orderContentGrid.asSingleSelect().clear();
        // actionForm.setUserToEdit(new CustomerOrder());
	}

	private void selectRow(ValueChangeEvent<CustomerOrder> event) {
		if (event.getValue() == null) {
            // actionForm.setVisible(false);
        } else {
            // actionForm.setUserToEdit(event.getValue());
        }
	}
}