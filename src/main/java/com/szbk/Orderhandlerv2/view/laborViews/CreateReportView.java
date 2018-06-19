package com.szbk.Orderhandlerv2.view.laborViews;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.szbk.Orderhandlerv2.controller.CustomerController;
import com.szbk.Orderhandlerv2.controller.OrderController;
import com.szbk.Orderhandlerv2.helpers.labor.CreateFileForDownload;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@SpringView(name = "createReport")
public class CreateReportView extends VerticalLayout implements View {
    private CustomerController customerController;
    private OrderController orderController;

    private CssLayout filteringLayout;
    private ComboBox<String> filterByCustomer;
    private Button selectCustomerBtn;
    private Grid<CustomerOrder> ordersForCustomerGrid;
    private Button exportFileBtn;

    private MultiSelect<CustomerOrder> multiSelectOnGrid;
    
    private FileDownloader fileDownloader;

    private ArrayList<String> customerNamesAndEmail;
    private StreamResource fileToDownload;
    private CreateFileForDownload createFile;

    @Autowired
    public CreateReportView(CustomerController customerController, OrderController orderController) {
        this.customerController = customerController;
        this.orderController = orderController;

        filterByCustomer = new ComboBox<>();
        selectCustomerBtn = new Button();
        ordersForCustomerGrid = new Grid<>(CustomerOrder.class);
        exportFileBtn = new Button("Exportálás");
        //When you add components to a layout, one of the component's methods will be called.
        //If you try doing it before the new, it will end with a NullPointerException.
        filteringLayout = new CssLayout(filterByCustomer, selectCustomerBtn);
        createFile = new CreateFileForDownload(customerController);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        //Checks, whether the user role is set or not. If not, or doesn't equals with the given string, redirect
        if ((VaadinSession.getCurrent().getAttribute("role") == null) || (!VaadinSession.getCurrent().getAttribute("role").equals("laboruser"))) {
            getUI().getNavigator().navigateTo("");
        } else {
            getUI().getPage().setTitle("Report készítése");
            setupContent();
        }
    }

	private void setupContent() {
        customerNamesAndEmail = customerController.getAllCustomersNameAndEmail();

        //view settings
        addComponents(filteringLayout, ordersForCustomerGrid, exportFileBtn);
        setSizeFull();
        setComponentAlignment(filteringLayout, Alignment.TOP_CENTER);
        setComponentAlignment(exportFileBtn, Alignment.BOTTOM_CENTER);
        setExpandRatio(ordersForCustomerGrid, 1);

        //filtering layout settings
        filteringLayout.setWidth(100, Unit.PERCENTAGE);
        filteringLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        //filter by customer settings
        filterByCustomer.setWidth(50, Unit.PERCENTAGE);
        filterByCustomer.setEmptySelectionAllowed(false);
        filterByCustomer.setPlaceholder("Szűrés megrendelő alapján");
        filterByCustomer.setStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
        filterByCustomer.setItems(customerNamesAndEmail);
        // filterByCustomer.addValueChangeListener(e -> );

        //select customer btn settings
        selectCustomerBtn.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_PRIMARY);
        selectCustomerBtn.setIcon(VaadinIcons.REFRESH);
        selectCustomerBtn.addClickListener(e -> listOrdersForCustomer());

        //grid settings
        ordersForCustomerGrid.setSizeFull();
        ordersForCustomerGrid.setColumns("sequenceName", "editedSequence", "scale", "purification", "type");
        ordersForCustomerGrid.getColumn("sequenceName").setCaption("Szekvencia neve");
        ordersForCustomerGrid.getColumn("sequenceName").setMaximumWidth(300);
        //The (sz) means the sequence displayed in the table is the edited one and not the original, which came from the customer
        ordersForCustomerGrid.getColumn("editedSequence").setCaption("Szekvencia (sz)");
        ordersForCustomerGrid.getColumn("editedSequence").setMaximumWidth(300);
        ordersForCustomerGrid.getColumn("scale").setCaption("Scale (nmol)");
        ordersForCustomerGrid.getColumn("scale").setMaximumWidth(100);
        ordersForCustomerGrid.getColumn("purification").setCaption("Kért tisztítás");
        ordersForCustomerGrid.getColumn("type").setCaption("Szekvencia típusa");

        //Multiselect for grid
        ordersForCustomerGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        multiSelectOnGrid = ordersForCustomerGrid.asMultiSelect();
        multiSelectOnGrid.addSelectionListener(e -> {
            Set<CustomerOrder> selectedItems = multiSelectOnGrid.getSelectedItems();
            if (selectedItems.size() > 0) {
//                createJobBtn.setEnabled(true);
                allowDownload(selectedItems);
            } else {
                exportFileBtn.setEnabled(false);
            }
        });

        //Export btn settings.
        exportFileBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        exportFileBtn.setEnabled(false);
    }

    private void allowDownload(Set<CustomerOrder> selectedItems) {
        String[] splitSelectedItem = filterByCustomer.getValue().split(" ");
        exportFileBtn.setEnabled(true);
        createFile.setOrdersToExport(selectedItems);
        createFile.setCustomerNameAsOwner(splitSelectedItem[1] + " " + splitSelectedItem[2]);
        fileToDownload = createFile.createResourceForDownload();
        fileDownloader = new FileDownloader(fileToDownload);
        fileDownloader.extend(exportFileBtn);

        exportFileBtn.addClickListener(e -> saveChangesOnOrders(selectedItems));
    }

    private void saveChangesOnOrders(Set<CustomerOrder> selectedItems) {
        exportFileBtn.setEnabled(false);
        multiSelectOnGrid.clear();
        orderController.changeStatusOnOrders(selectedItems, "Elküldve");
    }
    
    private void listOrdersForCustomer() {
        //String.charAt returns a char, but I need a string, so I have to use String.valueOf as well
        String[] selectedItem = filterByCustomer.getValue().split(":");
        long customerId = Long.parseLong(selectedItem[0]);
        System.out.println("customerId[report]: " + customerId);
        List<CustomerOrder> ordersForCustomer = orderController.getAllOrdersByCustomerId(customerId);
        ordersForCustomerGrid.setItems(ordersForCustomer);
    }
}