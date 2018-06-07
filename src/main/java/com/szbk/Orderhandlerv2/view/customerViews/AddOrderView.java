package com.szbk.Orderhandlerv2.view.customerViews;

import com.szbk.Orderhandlerv2.controller.OrderController;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@SpringComponent
@SpringView(name = "addOrder")
public class AddOrderView extends VerticalLayout implements View {
    private OrderController orderController;

    private SimpleOrderForm simpleOrderForm;

    private HorizontalLayout controlsLayout;
    private HorizontalLayout gridAndFormLayout;
    private Button newOrderBtn;
    private Button dropOrdersFromGridBtn;
    private Grid<CustomerOrder> newOrdersGrid;
    private Button sendOrdersBtn;

    private ArrayList<CustomerOrder> gridContentOrdersList;

    @Autowired
    public AddOrderView(OrderController orderController) {
        this.orderController = orderController;
        this.simpleOrderForm = new SimpleOrderForm(this, null, null);

        controlsLayout = new HorizontalLayout();
        gridAndFormLayout = new HorizontalLayout();
        newOrdersGrid = new Grid<>(CustomerOrder.class);
        newOrderBtn = new Button("Új megrendelés felvétele");
        dropOrdersFromGridBtn = new Button("Táblázat tartalmának elvetése");
        gridContentOrdersList = new ArrayList<>();
        sendOrdersBtn = new Button("A táblázatba felvett megrendelések leadása");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        addComponent(new Label("It is working!"));
        System.out.println("Entering order view");
        setupContent();
    }

    private void setupContent() {
        //View settings.
        setWidth(100, Unit.PERCENTAGE);
        addComponents(controlsLayout, gridAndFormLayout, sendOrdersBtn);
        setComponentAlignment(controlsLayout, Alignment.TOP_CENTER);
        setComponentAlignment(sendOrdersBtn, Alignment.BOTTOM_CENTER);

        //New order btn settings.
        newOrderBtn.addStyleNames(ValoTheme.BUTTON_FRIENDLY, ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
        newOrderBtn.setIcon(VaadinIcons.PLUS);
        newOrderBtn.addClickListener(e -> newOrderClick());

        //Grid and form settings.
        gridAndFormLayout.addComponents(newOrdersGrid, simpleOrderForm);
        gridAndFormLayout.setSizeFull();
        gridAndFormLayout.setExpandRatio(newOrdersGrid, 1);

        //New orders grid settings.
        newOrdersGrid.setSizeFull();
        newOrdersGrid.setColumns("type", "sequenceName", "sequence", "purification", "scale");
        newOrdersGrid.getColumn("type").setCaption("Típus");
        newOrdersGrid.getColumn("sequenceName").setCaption("Szekvencia neve");
        newOrdersGrid.getColumn("sequence").setCaption("Szekvencia");
        newOrdersGrid.getColumn("purification").setCaption("Tisztítás típusa");
        newOrdersGrid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() == null) {
                simpleOrderForm.setVisible(false);
            } else {
                simpleOrderForm.setOrderToEdit(e.getValue(), false);
            }
        });

        //Send orders btn settings.
        sendOrdersBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        sendOrdersBtn.addClickListener(e -> orderController.saveManyOrder(gridContentOrdersList));

        //Controls layout settings.
        controlsLayout.setSizeUndefined();
        controlsLayout.addComponents(newOrderBtn, dropOrdersFromGridBtn);
    }

    private void newOrderClick() {
        //Add new row to the grid.
        System.out.println("New order btn was clicked");
        newOrdersGrid.asSingleSelect().clear();
        simpleOrderForm.setOrderToEdit(new CustomerOrder(), true);
    }

    public void addOrderToTheGrid(CustomerOrder orderToAdd) {
        //If the item is in the list, remove it. Actually, I guess it won't work. Take a look!
        gridContentOrdersList.remove(orderToAdd);
        gridContentOrdersList.add(orderToAdd);
        newOrdersGrid.setItems(gridContentOrdersList);
    }

    public void removeOrderFromGrid(CustomerOrder orderToRemove) {
        gridContentOrdersList.remove(orderToRemove);
        newOrdersGrid.setItems(gridContentOrdersList);
    }
}
