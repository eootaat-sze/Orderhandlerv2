package com.szbk.Orderhandlerv2.view.customerViews;

import com.szbk.Orderhandlerv2.controller.OrderController;
import com.szbk.Orderhandlerv2.controller.OrderTypeController;
import com.szbk.Orderhandlerv2.controller.PurificationController;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.event.selection.SingleSelectionEvent;
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
    private static final String SINGLE_ORDER = "Egy megrendelés";
    private static final String MULTIPLE_ORDER = "Fájl feltöltés";
    private static final String DROP_CONTENT = "Táblázat tartalmának elvetése";
    private static final String SEND_ORDERS = "Megrendelések elküldése";

    private OrderController orderController;
    private OrderTypeController orderTypeController;
    private PurificationController purificationController;

    private SimpleOrderForm simpleOrderForm;
    private FileUploadWindow uploadWindow;

    private HorizontalLayout controlsLayout;
    private HorizontalLayout gridAndFormLayout;
    private HorizontalLayout sendAndDropBtnLayout;
    private ComboBox<String> orderQuantitySelect; 
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
        sendAndDropBtnLayout = new HorizontalLayout();
        orderQuantitySelect = new ComboBox<>();
        dropOrdersFromGridBtn = new Button(DROP_CONTENT);
        gridContentOrdersList = new ArrayList<>();
        sendOrdersBtn = new Button(SEND_ORDERS);
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
        addComponents(controlsLayout, gridAndFormLayout, sendAndDropBtnLayout);
        setComponentAlignment(controlsLayout, Alignment.TOP_CENTER);
        setComponentAlignment(sendAndDropBtnLayout, Alignment.BOTTOM_CENTER);
        setExpandRatio(gridAndFormLayout, 1);

        //Simple or multiple order type selection
        orderQuantitySelect.setItems(SINGLE_ORDER, MULTIPLE_ORDER);
        orderQuantitySelect.setWidth(50, Unit.PERCENTAGE);
        orderQuantitySelect.setEmptySelectionAllowed(false);
        orderQuantitySelect.setPlaceholder("Egy megrendelés vagy fájl feltöltés?");
        orderQuantitySelect.addSelectionListener(e -> handleQuantitySelection(e));

        //Drop from grid btn settings.
        dropOrdersFromGridBtn.addStyleNames(ValoTheme.BUTTON_DANGER, ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
        dropOrdersFromGridBtn.setIcon(VaadinIcons.CLOSE);
        dropOrdersFromGridBtn.addClickListener(e -> dropGridContent());

        //Grid and form settings.
        gridAndFormLayout.addComponents(newOrdersGrid, simpleOrderForm);
        gridAndFormLayout.setSizeFull();
        gridAndFormLayout.setExpandRatio(newOrdersGrid, 1);

        //Send and drop layout settings.
        sendAndDropBtnLayout.addComponents(sendOrdersBtn, dropOrdersFromGridBtn);
        sendAndDropBtnLayout.setComponentAlignment(sendOrdersBtn, Alignment.BOTTOM_LEFT);
        sendAndDropBtnLayout.setComponentAlignment(dropOrdersFromGridBtn, Alignment.BOTTOM_RIGHT);

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
        sendOrdersBtn.setIcon(VaadinIcons.CHECK);
        sendOrdersBtn.addClickListener(e -> {
            saveOrders();
        });

        //Controls layout settings.
        controlsLayout.setWidth(100, Unit.PERCENTAGE);
        // controlsLayout.addComponents(orderQuantitySelect, dropOrdersFromGridBtn);
        controlsLayout.addComponents(orderQuantitySelect);
        controlsLayout.setComponentAlignment(orderQuantitySelect, Alignment.TOP_CENTER);
    }

    private void handleQuantitySelection(SingleSelectionEvent<String> e) {
        if (e.getValue().equals(SINGLE_ORDER)) {
            displayNewOrderForm();
        } else {
            createFileUploadWindow();
        }
    }

	private void createFileUploadWindow() {
        if (uploadWindow == null) {
            uploadWindow = new FileUploadWindow(this);
        }

        if (simpleOrderForm != null && simpleOrderForm.isVisible()) {
            simpleOrderForm.setVisible(false);
        }
        getUI().addWindow(uploadWindow);
    }

    private void saveOrders() {
        if (orderController.saveManyOrder(gridContentOrdersList)) {
            Notification succesNotification = new Notification("Sikeres rendelés!");
            succesNotification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
            succesNotification.setPosition(Position.TOP_CENTER);
            succesNotification.show(getUI().getPage());
            dropGridContent();
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

	private void displayNewOrderForm() {
        //Add new row to the grid.
        newOrdersGrid.asSingleSelect().clear();
        simpleOrderForm.setOrderToEdit(new CustomerOrder(), true);
    }

    public void addOrderToTheGrid(CustomerOrder orderToAdd) {
        //If the item is in the list, remove it. Actually, I guess it won't work. Take a look!
        gridContentOrdersList.remove(orderToAdd);
        gridContentOrdersList.add(orderToAdd);
        newOrdersGrid.setItems(gridContentOrdersList);
    }

    public void addOrdersToTheGrid(List<CustomerOrder> orderstoAdd) {
        gridContentOrdersList.addAll(orderstoAdd);
        newOrdersGrid.setItems(gridContentOrdersList);
    }

    public void removeOrderFromGrid(CustomerOrder orderToRemove) {
        gridContentOrdersList.remove(orderToRemove);
        newOrdersGrid.setItems(gridContentOrdersList);
    }

    public void addOrderFromFileToGrid(String orders) {
        List<CustomerOrder> uploadedOrders = orderController.processUploadedFileAsString(orders);
        uploadedOrders = orderController.setCustomerNameAndIdToOrders(uploadedOrders, VaadinSession.getCurrent());
        if (uploadedOrders != null) {
            System.out.println();
            uploadedOrders.forEach(item -> System.out.println(item + "\n"));
            Notification.show("Sikeres feltöltés!");
            addOrdersToTheGrid(uploadedOrders);
        }
        getUI().removeWindow(uploadWindow);
    }
}
