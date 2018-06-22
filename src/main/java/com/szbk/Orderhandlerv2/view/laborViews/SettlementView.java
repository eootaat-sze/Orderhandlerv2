package com.szbk.Orderhandlerv2.view.laborViews;

import java.util.ArrayList;
import java.util.List;

import com.szbk.Orderhandlerv2.controller.CustomerController;
import com.szbk.Orderhandlerv2.controller.OrderController;
import com.szbk.Orderhandlerv2.helpers.labor.CreateDownloadFileForSettlementPage;
import com.szbk.Orderhandlerv2.model.Entity.Customer;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.data.HasValue.ValueChangeEvent;
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
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@SpringView(name = "settlementView")
public class SettlementView extends VerticalLayout implements View {
    private CustomerController customerController;
    private OrderController orderController;
    private CreateDownloadFileForSettlementPage createFileToDownload;

    private HorizontalLayout filteringLayout;
    private ComboBox<String> companyNameSelect;
    private ComboBox<String> groupNameSelect;
    private ComboBox<String> customerNameSelect;
    private Grid<CustomerOrder> filteredOrdersGrid;
    private Button downloadBtn;

    private FileDownloader fileDownloader;
    private StreamResource fileToDownload;

    private List<String> companyNames;
    private String selectedCompanyName;
    private String selectedGroupName;
    private String selectedCustomerName;
    private List<CustomerOrder> filteredOrders;

    @Autowired
    public SettlementView(CustomerController customerController, OrderController orderController) {
        this.customerController = customerController;
        this.orderController = orderController;

        companyNameSelect = new ComboBox<>();
        groupNameSelect = new ComboBox<>();
        customerNameSelect = new ComboBox<>();
        filteredOrdersGrid = new Grid<>(CustomerOrder.class);
        downloadBtn = new Button("Elszámolási fájl letöltése");
        filteringLayout = new HorizontalLayout(companyNameSelect, groupNameSelect, customerNameSelect);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        if ((VaadinSession.getCurrent().getAttribute("role") == null) || (!VaadinSession.getCurrent().getAttribute("role").equals("laboruser"))) {
            getUI().getNavigator().navigateTo("");
        } else {
            getUI().getPage().setTitle("Elszámolás készítése");
            setupContent();
        }
    }

	private void setupContent() {
        companyNames = customerController.getCompanyNames();

        //View settings.
        setSizeFull();
        addComponents(filteringLayout, filteredOrdersGrid, downloadBtn);
        setComponentAlignment(filteringLayout, Alignment.TOP_CENTER);
        setComponentAlignment(downloadBtn, Alignment.BOTTOM_CENTER);
        setExpandRatio(filteredOrdersGrid, 1);

        //Filtering layout settings
        filteringLayout.setWidth(100, Unit.PERCENTAGE);
        // filteringLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        //Company name select settings
        companyNameSelect.setWidth(100, Unit.PERCENTAGE);
        companyNameSelect.setEmptySelectionAllowed(false);
        companyNameSelect.setPlaceholder("Cég kiválasztása");
        companyNameSelect.setStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
        companyNameSelect.setDescription("Cég kiválasztása");
        companyNameSelect.setItems(companyNames);
        companyNameSelect.addValueChangeListener(event -> companyNameSelectListener(event));

        //Groupname select settings
        groupNameSelect.setWidth(100, Unit.PERCENTAGE);
        groupNameSelect.setEmptySelectionAllowed(false);
        groupNameSelect.setPlaceholder("Csoport kiválasztása");
        groupNameSelect.setStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
        groupNameSelect.setDescription("Csoport kiválasztása");
        groupNameSelect.addValueChangeListener(event -> groupNameSelectListener(event));
        groupNameSelect.setEnabled(false);

        //Customername select settings
        customerNameSelect.setWidth(100, Unit.PERCENTAGE);
        customerNameSelect.setEmptySelectionAllowed(false);
        customerNameSelect.setPlaceholder("Megrendelő kiválasztása");
        customerNameSelect.setStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
        customerNameSelect.setDescription("Megrendelő kiválasztása");
        customerNameSelect.addValueChangeListener(event -> customerNameSelectListener(event));
        customerNameSelect.setEnabled(false);

        //orders grid settings
        filteredOrdersGrid.setSizeFull();
        filteredOrdersGrid.setColumns("sequenceName", "price", "status");
        filteredOrdersGrid.getColumn("sequenceName").setCaption("Szekvencia neve");
        filteredOrdersGrid.getColumn("price").setCaption("Rendelés ára");
        filteredOrdersGrid.getColumn("status").setCaption("Rendelés státusza");

        //Download btn settings
        downloadBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        downloadBtn.setEnabled(false);

    }

	private void companyNameSelectListener(ValueChangeEvent<String> event) {
        selectedCompanyName = event.getValue();
        List<String> groupNames = customerController.getGroupNamesByCompanyName(selectedCompanyName);
        groupNameSelect.clear();
        groupNameSelect.setItems(groupNames);
        groupNameSelect.setEnabled(true);
        customerNameSelect.setEnabled(false);
    }
    
    private void groupNameSelectListener(ValueChangeEvent<String> event) {
        selectedGroupName = event.getValue();
        customerNameSelect.clear();

        if (selectedGroupName != null) {
            List<String> customerNames = customerController.getCustomerNamesByCompanyNameAndGroupName(selectedCompanyName, selectedGroupName);
            customerNameSelect.setItems(customerNames);
            customerNameSelect.setEnabled(true);
        }
    }
    
    private void customerNameSelectListener(ValueChangeEvent<String> event) {
        selectedCustomerName = event.getValue();
        
        if (selectedCustomerName != null) {
            Customer c = customerController.getCustomerByCompanyNameAndGroupNameAndCustomerName(selectedCompanyName, selectedGroupName, selectedCustomerName);
            filteredOrders = orderController.getAllOrdersByCustomerId(c.getId());
            filteredOrdersGrid.setItems(filteredOrders);

            if (filteredOrders.isEmpty()) {
                downloadBtn.setEnabled(false);
            } else {
                downloadBtn.setEnabled(true);
                createAndDownloadFile();
            }
        } else {
            filteredOrders = new ArrayList<>();
            filteredOrdersGrid.setItems(filteredOrders);
            downloadBtn.setEnabled(false);
        }
	}

	private void createAndDownloadFile() {
        //TODO Create file for download
        createFileToDownload = new CreateDownloadFileForSettlementPage(customerController, selectedCompanyName,
                                                                        selectedGroupName, selectedCustomerName);
        createFileToDownload.setOrdersToExport(filteredOrders);
        fileToDownload = createFileToDownload.createResourceForDownload();
        fileDownloader = new FileDownloader(fileToDownload);
        fileDownloader.extend(downloadBtn);
        downloadBtn.addClickListener(event -> saveChangesOnOrders(filteredOrders));
    }
    
    private void saveChangesOnOrders(List<CustomerOrder> selectedItems) {
        downloadBtn.setEnabled(false);
        orderController.changeStatusOnOrders(selectedItems, "Fizetve");
    }
}