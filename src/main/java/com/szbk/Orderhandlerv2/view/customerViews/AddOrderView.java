package com.szbk.Orderhandlerv2.view.customerViews;

import com.szbk.Orderhandlerv2.controller.OrderController;
import com.szbk.Orderhandlerv2.controller.OrderTypeController;
import com.szbk.Orderhandlerv2.controller.PurificationController;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

@SpringComponent
@SpringView(name = "addOrder")
public class AddOrderView extends VerticalLayout implements View {
    private OrderController orderController;
    private OrderTypeController orderTypeController;
    private PurificationController purificationController;

    private SimpleOrderForm simpleOrderForm;

    private HorizontalLayout controlsLayout;
    private HorizontalLayout gridAndFormLayout;
    private Button newOrderBtn;
    private Button dropOrdersFromGridBtn;
    private Grid<CustomerOrder> newOrdersGrid;
    private Button sendOrdersBtn;

    private ArrayList<CustomerOrder> gridContentOrdersList;

    @Autowired
    public AddOrderView(OrderController orderController, OrderTypeController orderTypeController,
                        PurificationController purificationController) {
        this.orderController = orderController;
        this.orderTypeController = orderTypeController;
        this.purificationController = purificationController;

        this.simpleOrderForm = new SimpleOrderForm(this, orderTypeController.getTypeNames(),
                purificationController.getPurificationNames());

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
        if ((VaadinSession.getCurrent().getAttribute("role") == null) || (!VaadinSession.getCurrent().getAttribute("role").equals("customer"))) {
            getUI().getNavigator().navigateTo("");
            System.out.println("No session");
        } else {
            getUI().getPage().setTitle("Rendelés leadása");
            setupContent();
        }
    }

    private void setupContent() {
        //View settings.
        setSizeFull();
        addComponents(controlsLayout, gridAndFormLayout, sendOrdersBtn);
        setComponentAlignment(controlsLayout, Alignment.TOP_CENTER);
        setComponentAlignment(sendOrdersBtn, Alignment.BOTTOM_CENTER);
        setExpandRatio(gridAndFormLayout, 1);

        //New order btn settings.
        newOrderBtn.addStyleNames(ValoTheme.BUTTON_FRIENDLY, ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
        newOrderBtn.setIcon(VaadinIcons.PLUS);
        newOrderBtn.addClickListener(e -> newOrderClick());

        //Drop from grid btn settings.
        dropOrdersFromGridBtn.addStyleNames(ValoTheme.BUTTON_DANGER, ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
        dropOrdersFromGridBtn.setIcon(VaadinIcons.CLOSE);
        dropOrdersFromGridBtn.addClickListener(e -> dropGridContent());

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
        sendOrdersBtn.addClickListener(e -> {
            saveOrders();
        });

        //Controls layout settings.
        controlsLayout.setSizeUndefined();
        controlsLayout.addComponents(newOrderBtn, dropOrdersFromGridBtn);
    }

    private void saveOrders() {
        if (orderController.saveManyOrder(gridContentOrdersList)) {
            Notification succesNotification = new Notification("Sikeres rendelés!");
            succesNotification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
            succesNotification.setPosition(Position.TOP_CENTER);
            succesNotification.show(getUI().getPage());
        } else {
            Notification warningNotification = new Notification("Sikertelen rendelés! Próbálja meg később!");
            warningNotification.setStyleName(ValoTheme.NOTIFICATION_ERROR);
            warningNotification.setPosition(Position.TOP_CENTER);
            warningNotification.show(getUI().getPage());
        }
	}

	//If the customer wants to drop the content of the grid, set the list to a new arraylist and add it to the grid.
    private void dropGridContent() {
        gridContentOrdersList = new ArrayList<>();
        newOrdersGrid.setItems(gridContentOrdersList);
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
