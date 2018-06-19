package com.szbk.Orderhandlerv2.view.laborViews;

import java.util.List;
import java.util.Set;

import com.szbk.Orderhandlerv2.controller.CustomerController;
import com.szbk.Orderhandlerv2.controller.OrderController;
import com.szbk.Orderhandlerv2.controller.OrderStatusController;
import com.szbk.Orderhandlerv2.helpers.labor.JobCreation;
import com.szbk.Orderhandlerv2.model.Entity.Customer;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@SpringView(name = "ordersView")
public class AllOrdersView extends VerticalLayout implements View {
    private OrderController orderController;
    private CustomerController customerController;
    private OrderStatusController orderStatusController;

    private ComboBox<String> filterByStatus;
    private Button clearFilterBtn;
    private Grid<CustomerOrder> ordersGrid;
    private Button createJobBtn;
    private CssLayout filteringLayout;
    private TextField sequenceEditField;

    private Binder<CustomerOrder> dataBinder;
    private MultiSelect<CustomerOrder> multiSelectOnGrid;

    private JobCreation createJob;

    private List<String> statusList;
    private List<CustomerOrder> filteredOrders;

    private FileDownloader fileDownloader;
    private StreamResource fileToDownload;
    
    @Autowired
    public AllOrdersView(OrderController orderController, CustomerController customerController,
            OrderStatusController orderStatusController) {
        this.orderController = orderController;
        this.customerController = customerController;
        this.orderStatusController = orderStatusController;

        filterByStatus = new ComboBox<>();
        clearFilterBtn = new Button();
        ordersGrid = new Grid<>(CustomerOrder.class);
        createJobBtn = new Button("Job létrehozása a kiválasztott elemekből");
        filteringLayout = new CssLayout(filterByStatus, clearFilterBtn);
        sequenceEditField = new TextField();
        dataBinder = ordersGrid.getEditor().getBinder();
        createJob = new JobCreation(customerController);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        if ((VaadinSession.getCurrent().getAttribute("role") == null) || (!VaadinSession.getCurrent().getAttribute("role").equals("laboruser"))) {
            getUI().getNavigator().navigateTo("");
        } else {
            getUI().getPage().setTitle("Összes megrendelés");
            setupContent();
        }
    }

	private void setupContent() {
        statusList = orderStatusController.getAllStatusNames();

        //View settings.
        setSizeFull();
        addComponents(filteringLayout, ordersGrid, createJobBtn);
        setComponentAlignment(filteringLayout, Alignment.TOP_CENTER);
        setComponentAlignment(createJobBtn, Alignment.BOTTOM_CENTER);
        setExpandRatio(ordersGrid, 1);

        //Filtering layout settings.
        filteringLayout.setWidth(100, Unit.PERCENTAGE);
        filteringLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        //Filter by status settings.
        filterByStatus.setWidth(50, Unit.PERCENTAGE);
        filterByStatus.setEmptySelectionAllowed(false);
        filterByStatus.setPlaceholder("Szűrés státusz alapján...");
        filterByStatus.setStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
        filterByStatus.setItems(statusList);
        filterByStatus.addSelectionListener(e -> refreshGridContent());

        //Clear filter btn settings.
        clearFilterBtn.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        clearFilterBtn.setIcon(VaadinIcons.CLOSE);
        clearFilterBtn.addClickListener(e -> filterByStatus.clear());

        //Orders grid settings.
        ordersGrid.setSizeFull();
        ordersGrid.setColumns("sequenceName", "sequence", "editedSequence", "scale", "purification", "type", "status", "orderDate");
        ordersGrid.getColumn("sequenceName").setCaption("Rendelés neve");
        ordersGrid.getColumn("sequenceName").setMaximumWidth(300);
        ordersGrid.getColumn("sequence").setCaption("Szekvencia");
        ordersGrid.getColumn("sequence").setExpandRatio(1);
        ordersGrid.getColumn("sequence").setMaximumWidth(300);
        ordersGrid.getColumn("editedSequence").setCaption("Szerkeszthető szekvencia");
        ordersGrid.getColumn("editedSequence").setMaximumWidth(300);
        ordersGrid.getColumn("scale").setCaption("Scale (nmol)");
        ordersGrid.getColumn("scale").setMaximumWidth(100);
        ordersGrid.getColumn("purification").setCaption("Kért tisztítás");
        ordersGrid.getColumn("type").setCaption("Szekvencia típusa");
        ordersGrid.getColumn("status").setCaption("Rendelés státusza");
        ordersGrid.getColumn("orderDate").setCaption("Rendelés dátuma");
        dataBinder.forField(sequenceEditField).asRequired("A szekvencia mező nem lehet üres!")
                .bind(CustomerOrder::getEditedSequence, CustomerOrder::setEditedSequence);
        ordersGrid.getColumn("editedSequence").setEditorComponent(sequenceEditField);
        ordersGrid.getEditor().addSaveListener(e -> {
            validateAndSaveSequenceString(e.getBean());
            
        });
        ordersGrid.getEditor().setEnabled(true);
        ordersGrid.setItems(orderController.getAllOrders());

        //Setting multiselect to the grid.
        ordersGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        multiSelectOnGrid = ordersGrid.asMultiSelect();
        multiSelectOnGrid.addSelectionListener(e -> {
            Set<CustomerOrder> selectedItems = multiSelectOnGrid.getSelectedItems();
            if (selectedItems.size() > 0 && selectedItems.size() < 24) {
//                createJobBtn.setEnabled(true);
                allowDownload(selectedItems);
            } else {
                createJobBtn.setEnabled(false);
            }
        });

        //Job btn settings.
        createJobBtn.setEnabled(false);
//        createJobBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
//        createJobBtn.setEnabled(false);
//        fileToDownload = createJob.createResourceForDownload();
//        createJobBtn.addClickListener(e -> saveChangesOnOrders());
	}

	private void allowDownload(Set<CustomerOrder> selectedItems) {
        createJobBtn.setEnabled(true);
        createJob.setOrdersForJob(selectedItems);
        fileToDownload = createJob.createResourceForDownload();
        fileDownloader = new FileDownloader(fileToDownload);
        fileDownloader.extend(createJobBtn);

        createJobBtn.addClickListener(e -> saveChangesOnOrders(selectedItems));
    }

	private void saveChangesOnOrders(Set<CustomerOrder> selectedItems) {
        createJobBtn.setEnabled(false);
        multiSelectOnGrid.clear();
        orderController.changeStatusOnOrders(selectedItems, "Folyamatban");
    }
    
    //After editing the sequence this method will be called for validation and saving the edit if it's valid.
    private void validateAndSaveSequenceString(CustomerOrder order) {
        boolean validationRes = validateSequenceString(order.getType(), order.getEditedSequence());
        if (validationRes) {
            orderController.saveOrder(order);
        } else {
            String warningContent = "A szerkesztett szekvencia formátuma nem illeszkedik a kiválasztott típushoz! Nem történt mentés.";
            Notification invalidSequenceNotification = new Notification(warningContent);
            invalidSequenceNotification.setStyleName(ValoTheme.NOTIFICATION_WARNING);
            invalidSequenceNotification.setPosition(Position.TOP_CENTER);
            invalidSequenceNotification.setDelayMsec(10000);
            invalidSequenceNotification.show(getUI().getPage());
        }
    }
    
    //Do the validation stuff on the edited sequence.
    private boolean validateSequenceString(String type, String sequenceToValidate) {
        if (type.equals("DNS")) {
            sequenceToValidate = sequenceToValidate.replace("A", "x").replace("C", "x")
                    .replace("G", "x").replace("T", "x");

            if (sequenceToValidate.matches("x{" + sequenceToValidate.length() + "}")) {
                return true;
            } else {
                return false;
            }
        } else if (type.equals("RNS")) {
            sequenceToValidate = sequenceToValidate.replace("A", "x").replace("C", "x")
                    .replace("G", "x").replace("U", "x");

            if (sequenceToValidate.matches("x{" + sequenceToValidate.length() + "}")) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private void refreshGridContent() {
        //If the user picked something in the filter field, use it to filter the grid content.
        if (filterByStatus.getSelectedItem().isPresent()) {
            String selectedStatus = filterByStatus.getSelectedItem().get();
            filteredOrders = orderController.getOrdersByStatus(selectedStatus);
            ordersGrid.setItems(filteredOrders);
        } else {
			filteredOrders = orderController.getAllOrders();
			ordersGrid.setItems(filteredOrders);
        }
	}
}