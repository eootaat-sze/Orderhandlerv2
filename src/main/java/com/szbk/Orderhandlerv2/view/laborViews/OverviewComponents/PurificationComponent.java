package com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents;

import com.szbk.Orderhandlerv2.controller.PurificationController;
import com.szbk.Orderhandlerv2.model.Entity.Purification;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;

public class PurificationComponent extends HorizontalLayout {
    private PurificationController purificationController;
    private Grid<Purification> purificationContentGrid;

    public PurificationComponent(PurificationController typeController) {
        this.purificationController = typeController;
        purificationContentGrid = new Grid<>(Purification.class);

        setupContent();
    }

	private void setupContent() {
        //View settings
        setSizeFull();
        addComponents(purificationContentGrid);
        setExpandRatio(purificationContentGrid, 1);

        //Content grid settings
        purificationContentGrid.setSizeFull();
        purificationContentGrid.setColumnOrder("name", "price", "id");
        purificationContentGrid.getColumn("name").setCaption("Tisztítás neve");
        purificationContentGrid.getColumn("price").setCaption("Ára");
        // statusContentGrid.asSingleSelect().addValueChangeListener(event -> selectRow(event));
        updateGrid();
	}

	private void updateGrid() {
        purificationContentGrid.setItems(purificationController.getAllPurifications());
	}
}