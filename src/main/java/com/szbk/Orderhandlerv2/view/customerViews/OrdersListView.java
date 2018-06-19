package com.szbk.Orderhandlerv2.view.customerViews;

import java.util.List;

import com.szbk.Orderhandlerv2.controller.OrderController;
import com.szbk.Orderhandlerv2.controller.OrderStatusController;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = "ordersListView")
public class OrdersListView extends VerticalLayout implements View {
    private OrderController orderController;
    private OrderStatusController statusController;

    private ComboBox<String> filterByStatus;
    private Grid<CustomerOrder> ordersGrid;
    private Button clearFilteringBtn;
    private CssLayout filteringLayout;

    private List<CustomerOrder> filteredOrders;
    private List<String> statusList;
    
    @Autowired
    public OrdersListView(OrderController orderController, OrderStatusController statusController) {
        this.orderController = orderController;
        this.statusController = statusController;
        
        filterByStatus = new ComboBox<>();
        ordersGrid = new Grid<>(CustomerOrder.class);
        clearFilteringBtn = new Button();
        filteringLayout = new CssLayout(filterByStatus, clearFilteringBtn);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        //I guess I can change the page title with this. We'll see!
        if ((VaadinSession.getCurrent().getAttribute("role") == null) || (!VaadinSession.getCurrent().getAttribute("role").equals("customer"))) {
            getUI().getNavigator().navigateTo("");
            System.out.println("No session");
        } else {
            getUI().getPage().setTitle("Eddigi rendeléseim");
            setupContent();
        }
    }

	private void setupContent() {
        statusList = statusController.getAllStatusNames();

        //View settings.
        setSizeFull();
        addComponents(filteringLayout, ordersGrid);
        setComponentAlignment(filteringLayout, Alignment.TOP_CENTER);
        setExpandRatio(ordersGrid, 1);

        //Filtering layout settings.
        filteringLayout.setWidth(100, Unit.PERCENTAGE);
        filteringLayout.addStyleNames(ValoTheme.LAYOUT_COMPONENT_GROUP);

        //Filter by status combobox settings.
        filterByStatus.setWidth(50, Unit.PERCENTAGE);
        filterByStatus.setEmptySelectionAllowed(false);
        filterByStatus.setPlaceholder("Szűrés státusz alapján...");
        filterByStatus.setStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
        filterByStatus.setItems(statusList);
        filterByStatus.addSelectionListener(e -> refreshGridContent());

        //Clear filtering btn settings.
        clearFilteringBtn.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        clearFilteringBtn.setIcon(VaadinIcons.CLOSE);
        clearFilteringBtn.addClickListener(e -> filterByStatus.clear());

        //Orders grid settings.
        ordersGrid.setSizeFull();
        refreshGridContent();
        ordersGrid.setColumns("sequenceName", "orderDate", "status", "price", "sequence", "scale", "purification", "type");
        ordersGrid.getColumn("sequenceName").setCaption("Rendelés neve");
        ordersGrid.getColumn("orderDate").setCaption("Rendelés dátuma");
        ordersGrid.getColumn("status").setCaption("Rendelés státusza");
        ordersGrid.getColumn("price").setCaption("Rendelés ára");
        ordersGrid.getColumn("sequence").setCaption("Szekvencia");
        ordersGrid.getColumn("scale").setCaption("Scale (nmol)");
        ordersGrid.getColumn("purification").setCaption("Kért tisztítás");
        //The type is the type of the sequence or the type of... what? Just to know what to show here.
        ordersGrid.getColumn("type").setCaption("Szekvencia típusa");
        
        //
	}

	private void refreshGridContent() {
        long customerId = Long.parseLong(String.valueOf(VaadinSession.getCurrent().getAttribute("id")));
        
        //If the user picked something in the filter field, use it to filter the grid content.
        if (filterByStatus.getSelectedItem().isPresent()) {
            String selectedStatus = filterByStatus.getSelectedItem().get();

            if (selectedStatus.equals("Nincs szűrés")) {
                filteredOrders = orderController.getAllOrdersByCustomerId(customerId);
            } else {
                filteredOrders = orderController.filterOrdersByCustomerIdAndStatus(customerId, selectedStatus);
            }
            
            ordersGrid.setItems(filteredOrders);
        } else {
			filteredOrders = orderController.getAllOrdersByCustomerId(customerId);
			ordersGrid.setItems(filteredOrders);
        }
	}
}