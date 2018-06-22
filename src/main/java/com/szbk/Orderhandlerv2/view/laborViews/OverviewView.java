package com.szbk.Orderhandlerv2.view.laborViews;

import com.szbk.Orderhandlerv2.controller.CustomerController;
import com.szbk.Orderhandlerv2.controller.LaborUserController;
import com.szbk.Orderhandlerv2.controller.OrderController;
import com.szbk.Orderhandlerv2.controller.OrderStatusController;
import com.szbk.Orderhandlerv2.controller.OrderTypeController;
import com.szbk.Orderhandlerv2.controller.PurificationController;
import com.szbk.Orderhandlerv2.model.Entity.Customer;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.szbk.Orderhandlerv2.model.Entity.LaborUser;
import com.szbk.Orderhandlerv2.model.Entity.OrderStatus;
import com.szbk.Orderhandlerv2.model.Entity.Purification;
import com.szbk.Orderhandlerv2.model.Entity.Type;
import com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents.CustomerComponent;
import com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents.CustomerOrderComponent;
import com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents.LaboruserActionForm;
import com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents.LaboruserComponent;
import com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents.OrderTypeComponent;
import com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents.PurificationComponent;
import com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents.StatusComponent;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@SpringView(name = "overview")
public class OverviewView extends VerticalLayout implements View {
    private CustomerController customerController;
    private LaborUserController laborUserController;
    private OrderController orderController;
    private OrderStatusController orderStatusController;
    private OrderTypeController orderTypeController;
    private PurificationController purificationController;
    // private LaboruserActionForm laborActionForm;
    private LaboruserComponent laboruserComponent;
    private CustomerComponent customerComponent;
    private CustomerOrderComponent orderComponent;
    private OrderTypeComponent typeComponent;
    private StatusComponent statusComponent;
    private PurificationComponent purificationComponent;

    private CssLayout filteringLayout;
    private ComboBox<String> tableSelect;
    private Button addItemBtn;

    private String[] tableNames = {"Megrendelők", "Rendelések", "Laboros felhasználók", "Megadható típusok", "Megadható tisztítási típusok", "Lehetséges státuszok"};
    //I know this is not a good solution (not using generic type) but I can't set it here. It depends on the selection.
    private Grid<Customer> customerGrid;
    private Grid<CustomerOrder> ordersGrid;
    private Grid<LaborUser> laboruserGrid;
    private Grid<Type> typesGrid;
    private Grid<Purification> purificationGrid;
    private Grid<OrderStatus> statusGrid;
    // private HorizontalLayout gridAndFormLayout;

    @Autowired
    public OverviewView(CustomerController customerController, LaborUserController laborUserController,
        OrderController orderController, OrderStatusController orderStatusController,
        OrderTypeController orderTypeController, PurificationController purificationController) {
        this.customerController = customerController;
        this.laborUserController = laborUserController;
        this.orderController = orderController;
        this.orderStatusController = orderStatusController;
        this.orderTypeController = orderTypeController;
        this.purificationController = purificationController;

        // laborActionForm = new LaboruserActionForm(this, laborUserController);
        tableSelect = new ComboBox<>();
        // addItemBtn = new Button();
        filteringLayout = new CssLayout(tableSelect);
        laboruserComponent = new LaboruserComponent(laborUserController);
        customerComponent = new CustomerComponent(customerController);
        orderComponent = new CustomerOrderComponent(orderController);
        typeComponent = new OrderTypeComponent(orderTypeController);
        statusComponent = new StatusComponent(orderStatusController);
        purificationComponent = new PurificationComponent(purificationController);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        if ((VaadinSession.getCurrent().getAttribute("role") == null) || (!VaadinSession.getCurrent().getAttribute("role").equals("laboruser"))) {
            getUI().getNavigator().navigateTo("");
        } else {
            getUI().getPage().setTitle("Áttekintő nézet");
            setupContent();
        }

        // setupContent();
    }

	private void setupContent() {
        //View settings
        setSizeFull();
        addComponents(filteringLayout);
        setComponentAlignment(filteringLayout, Alignment.TOP_CENTER);

        //Filtering layout settings
        filteringLayout.setWidth(100, Unit.PERCENTAGE);
        filteringLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        //Table select settings
        tableSelect.setWidth(50, Unit.PERCENTAGE);
        tableSelect.addStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
        tableSelect.setPlaceholder("Tábla kiválasztása");
        tableSelect.setEmptySelectionAllowed(false);
        tableSelect.setItems(tableNames);
        tableSelect.addValueChangeListener(event -> refreshGridContent(event));
    }

	private void refreshGridContent(ValueChangeEvent<String> event) {
        String selectedTable = event.getValue();
        // "Laboros felhasználók", "Megadható típusok", "Megadható tisztítási típusok", "Lehetséges státuszok"
        switch (selectedTable) {
            case "Megrendelők":
                setupComponent(customerComponent);
                break;
            case "Rendelések":
                setupComponent(orderComponent);
                break;
            case "Laboros felhasználók":
                setupComponent(laboruserComponent);
                break;
            case "Megadható típusok":
                // addItemBtn.setEnabled(true);
                setupComponent(typeComponent);
                break;
            case "Megadható tisztítási típusok":
                // addItemBtn.setEnabled(true);
                setupComponent(purificationComponent);
                break;
            case "Lehetséges státuszok":
                // addItemBtn.setEnabled(true);
                setupComponent(statusComponent);
                break;
        }
    }

    //Setup the grid for customers and display it (in some way)
    private void setupCustomerGrid() {
        

        customerGrid.setItems(customerController.getAllCustomers());
        // addOrReplaceGrid(customerGrid);
        // setExpandRatio(customerGrid, 1);
    }

    private void setupOrdersGrid() {
        if (ordersGrid == null) {
            ordersGrid = new Grid<>(CustomerOrder.class);
            ordersGrid.setSizeFull();
            ordersGrid.setColumnOrder("sequenceName", "sequence", "editedSequence", "type", "purification", "scale", "price", "status", "orderDate", "customerId", "customerInnerId", "id");
            ordersGrid.getColumn("sequenceName").setCaption("Szekvencia neve");
            ordersGrid.getColumn("sequenceName").setMaximumWidth(300);
            ordersGrid.getColumn("sequence").setCaption("Szekvencia");
            ordersGrid.getColumn("sequence").setMaximumWidth(300);
            ordersGrid.getColumn("editedSequence").setCaption("Szekvencia (sz)");
            ordersGrid.getColumn("editedSequence").setMaximumWidth(300);
            ordersGrid.getColumn("type").setCaption("Típus");
            ordersGrid.getColumn("purification").setCaption("Tisztítás");
            ordersGrid.getColumn("price").setCaption("Ár (Forint)");
            ordersGrid.getColumn("status").setCaption("Státusz");
            ordersGrid.getColumn("orderDate").setCaption("Megrendelés dátuma");
            ordersGrid.getColumn("customerId").setCaption("Megrendelő azonosító");
            ordersGrid.getColumn("customerInnerId").setCaption("Belső azonosító");
        }

        ordersGrid.setItems(orderController.getAllOrders());
        // addOrReplaceGrid(ordersGrid);
        // setExpandRatio(ordersGrid, 1);
    }

    private void setupComponent(HorizontalLayout componentToAdd) {
        if (getComponentCount() > 1) {
            replaceComponent(getComponent(1), componentToAdd);
        } else if (getComponentCount() == 1) {
            addComponent(componentToAdd);
        }

        setExpandRatio(componentToAdd, 1);
    }

    private void setupTypesGrid() {
        if (typesGrid == null) {
            typesGrid = new Grid<>(Type.class);
            typesGrid.setSizeFull();
            typesGrid.setColumnOrder("name", "price", "id");
            typesGrid.getColumn("name").setCaption("Típus neve");
            typesGrid.getColumn("price").setCaption("Ára");
        }

        typesGrid.setItems(orderTypeController.getAllTypes());
        // addOrReplaceGrid(typesGrid);
    }

    private void setupPurificationGrid() {
        if (purificationGrid == null) {
            purificationGrid = new Grid<>(Purification.class);
            purificationGrid.setSizeFull();
            purificationGrid.setColumnOrder("name", "price", "id");
            purificationGrid.getColumn("name").setCaption("Tisztítás neve");
            purificationGrid.getColumn("price").setCaption("Ára");
        }

        purificationGrid.setItems(purificationController.getAllPurifications());
        // addOrReplaceGrid(purificationGrid);
    }

    private void setupStatusGrid() {
        if (statusGrid == null) {
            statusGrid = new Grid<>(OrderStatus.class);
            statusGrid.setSizeFull();
            statusGrid.setColumnOrder("name", "id");
            statusGrid.getColumn("name").setCaption("Rendelési státusz neve");
        }

        statusGrid.setItems(orderStatusController.getAllStatus());
        // addOrReplaceGrid(statusGrid);
    }
/*
    private void addOrReplaceGrid(Grid grid) {
        if (gridAndFormLayout.getComponentCount() == 0) {
            gridAndFormLayout.addComponent(grid);
            gridAndFormLayout.setExpandRatio(grid, 1);
            gridAndFormLayout.addComponent(laborActionForm);
            laborActionForm.setUserToEdit(new LaborUser());
        } else {
            gridAndFormLayout.removeAllComponents();
            gridAndFormLayout.addComponents(grid, laborActionForm);
            gridAndFormLayout.setExpandRatio(grid, 1);
        }
    //     if (getComponentCount() == 1) {
    //         addComponent(grid);
    //     } else {
    //         replaceComponent(getComponent(1), grid);
    //     }

    //     setExpandRatio(grid, 1);
    }
*/

    public void updateLaboruserGrid() {
        if (laboruserGrid != null) {
            laboruserGrid.setItems(laborUserController.getAllLaborUsers());
        }
    }
}