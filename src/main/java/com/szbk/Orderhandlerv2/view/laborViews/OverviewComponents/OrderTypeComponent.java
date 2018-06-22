package com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents;

import com.szbk.Orderhandlerv2.controller.OrderTypeController;
import com.szbk.Orderhandlerv2.model.Entity.Type;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;

public class OrderTypeComponent extends HorizontalLayout {
    private OrderTypeController typeController;
    private Grid<Type> typeContentGrid;

    public OrderTypeComponent(OrderTypeController typeController) {
        this.typeController = typeController;
        typeContentGrid = new Grid<>(Type.class);

        setupContent();
    }

	private void setupContent() {
        //View settings
        setSizeFull();
        addComponents(typeContentGrid);
        setExpandRatio(typeContentGrid, 1);

        //Content grid settings
        typeContentGrid.setSizeFull();
        typeContentGrid.setColumnOrder("name", "price", "id");
        typeContentGrid.getColumn("name").setCaption("Típus neve");
        typeContentGrid.getColumn("price").setCaption("Ára");
        // statusContentGrid.asSingleSelect().addValueChangeListener(event -> selectRow(event));
        updateGrid();
	}

	private void updateGrid() {
        typeContentGrid.setItems(typeController.getAllTypes());
	}
}