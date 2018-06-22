package com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents;

import com.szbk.Orderhandlerv2.controller.OrderStatusController;
import com.szbk.Orderhandlerv2.model.Entity.OrderStatus;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;

public class StatusComponent extends HorizontalLayout {
    private OrderStatusController statusController;
    private Grid<OrderStatus> statusContentGrid;

    public StatusComponent(OrderStatusController controller) {
        statusController = controller;
        statusContentGrid = new Grid<>(OrderStatus.class);
        // addItemBtn = new Button("Új elem hozzáadása");
        // actionLayout = new VerticalLayout(addItemBtn);

        setupContent();
    }

	private void setupContent() {
        //View settings
        setSizeFull();
        addComponents(statusContentGrid);
        setExpandRatio(statusContentGrid, 1);

        //Content grid settings
        statusContentGrid.setSizeFull();
        statusContentGrid.setColumnOrder("name", "id");
        statusContentGrid.getColumn("name").setCaption("Rendelési státusz neve");
        // statusContentGrid.asSingleSelect().addValueChangeListener(event -> selectRow(event));
        updateGrid();

        //Action layout settings
        // actionLayout.setSizeUndefined();
        // actionLayout.setComponentAlignment(addItemBtn, Alignment.TOP_CENTER);

        //Add item btn settings
        // addItemBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        // addItemBtn.addClickListener(event -> newItemClick());
    }

	public void updateGrid() {
        statusContentGrid.setItems(statusController.getAllStatus());
	}

	private void newItemClick() {
        statusContentGrid.asSingleSelect().clear();
        // actionForm.setUserToEdit(new CustomerOrder());
	}

	private void selectRow(ValueChangeEvent<OrderStatus> event) {
		if (event.getValue() == null) {
            // actionForm.setVisible(false);
        } else {
            // actionForm.setUserToEdit(event.getValue());
        }
	}
}