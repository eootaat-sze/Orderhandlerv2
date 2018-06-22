package com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents;

import com.szbk.Orderhandlerv2.controller.LaborUserController;
import com.szbk.Orderhandlerv2.model.Entity.LaborUser;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class LaboruserComponent extends HorizontalLayout {
    private LaborUserController laborController;
    private LaboruserActionForm actionForm;

    private Grid<LaborUser> laborContentGrid;
    private VerticalLayout actionLayout;
    private Button addItemBtn;

    public LaboruserComponent(LaborUserController controller) {
        laborController = controller;
        actionForm = new LaboruserActionForm(this, laborController);
        laborContentGrid = new Grid<>(LaborUser.class);
        addItemBtn = new Button("Új elem hozzáadása");
        actionLayout = new VerticalLayout(addItemBtn, actionForm);

        setupContent();
    }

    private void setupContent() {
        //View settings
        setSizeFull();
        addComponents(laborContentGrid, actionLayout);
        setExpandRatio(laborContentGrid, 1);

        //Content grid settings
        laborContentGrid.setSizeFull();
        laborContentGrid.setColumnOrder("name", "email", "password", "id");
        laborContentGrid.getColumn("name").setCaption("Laboros felhasználó neve");
        laborContentGrid.getColumn("email").setCaption("Email cím");
        laborContentGrid.getColumn("password").setCaption("Jelszó");
        laborContentGrid.asSingleSelect().addValueChangeListener(event -> selectRow(event));
        updateGrid();

        //Action layout settings
        actionLayout.setSizeUndefined();
        actionLayout.setComponentAlignment(addItemBtn, Alignment.TOP_CENTER);

        //Add item btn settings
        addItemBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        addItemBtn.addClickListener(event -> newItemClick());
    }

	public void updateGrid() {
        laborContentGrid.setItems(laborController.getAllLaborUsers());
	}

	private void newItemClick() {
        laborContentGrid.asSingleSelect().clear();
        actionForm.setUserToEdit(new LaborUser());
	}

	private void selectRow(ValueChangeEvent<LaborUser> event) {
		if (event.getValue() == null) {
            actionForm.setVisible(false);
        } else {
            actionForm.setUserToEdit(event.getValue());
        }
	}
}