package com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents.Forms;

import com.szbk.Orderhandlerv2.controller.CustomerController;
import com.szbk.Orderhandlerv2.model.Entity.Customer;
import com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents.CustomerComponent;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CustomerActionForm extends VerticalLayout {
    private CustomerComponent view;
    private CustomerController controller;

    private Label formTitle;
    private TextField customerNameField;
    private TextField emailField;
    private TextField passwordField;
    private TextField groupNameField;
    private TextField companyNameField;
    private TextField innernameField;
    private Button saveBtn;
    private Binder<Customer> dataBinder;
    private Customer customerToEdit;

    public CustomerActionForm(CustomerComponent view, CustomerController controller) {
        this.view = view;
        this.controller = controller;
        
        formTitle = new Label("Hozzáadás/módosítás");
        customerNameField = new TextField();
        emailField = new TextField();
        passwordField = new TextField();
        groupNameField = new TextField();
        companyNameField = new TextField();
        innernameField = new TextField();
        saveBtn = new Button("Mentés");
        dataBinder = new Binder<>(Customer.class);

        setupContent();
    }

    private void setupContent() {
        //View settings
        setWidth(100, Unit.PERCENTAGE);
        setMargin(false);
        addComponents(formTitle, customerNameField, emailField, passwordField, groupNameField, companyNameField, innernameField, saveBtn);
        setComponentAlignment(formTitle, Alignment.TOP_CENTER);
        setComponentAlignment(saveBtn, Alignment.BOTTOM_CENTER);

        //Form title settings
        formTitle.addStyleNames(ValoTheme.LABEL_H3, ValoTheme.TEXTAREA_ALIGN_CENTER);
        formTitle.setWidth(100, Unit.PERCENTAGE);

        //Customername field settings
        customerNameField.setPlaceholder("Megrendelő neve");
        customerNameField.setWidth(100, Unit.PERCENTAGE);
        customerNameField.focus();
        dataBinder.forField(customerNameField).asRequired().bind(Customer::getCustomerName, Customer::setCustomerName);

        //Email field settings
        emailField.setPlaceholder("Email cím");
        emailField.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(emailField).asRequired().withValidator(
                new EmailValidator("Nem megfelelő email cím formátum!")
            ).bind(Customer::getEmail, Customer::setEmail);

        //Password field settings
        passwordField.setPlaceholder("Jelszó");
        passwordField.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(passwordField).asRequired().bind(Customer::getPassword, Customer::setPassword);

        //Groupname field settings
        groupNameField.setPlaceholder("Csoport neve");
        groupNameField.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(groupNameField).asRequired().bind(Customer::getGroupName, Customer::setGroupName);

        //Companyname field settings
        companyNameField.setPlaceholder("Cég neve");
        companyNameField.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(companyNameField).asRequired().bind(Customer::getCompanyName, Customer::setCompanyName);

        //Innername field settings
        innernameField.setPlaceholder("Belső azonosító");
        innernameField.setWidth(100, Unit.PERCENTAGE);
        innernameField.setEnabled(false);
        dataBinder.forField(innernameField).bind(Customer::getInnerName, Customer::setInnerName);

        //Save btn settings
        saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveBtn.setWidth(50, Unit.PERCENTAGE);
        saveBtn.addClickListener(event -> validateAndSaveCustomer());

        setVisible(false);
    }

    private void validateAndSaveCustomer() {
        try {
            dataBinder.writeBean(customerToEdit);

            if (controller.registration(customerToEdit)) {
                view.updateGrid();
                setVisible(false);
            }
        } catch (ValidationException e) {
            Notification notification = new Notification("Ellenőrizd a hibaüzeneteket az egyes mezők mellett!");
            notification.setStyleName(ValoTheme.NOTIFICATION_ERROR);
            notification.setPosition(Position.TOP_CENTER);
            notification.show(getUI().getPage());
        }
    }

    public void setCustomerToEdit(Customer customer) {
        if (customer != null) {
            customerToEdit = customer;
            dataBinder.setBean(customerToEdit);
            setVisible(true);
        } else {
            System.out.println("Something went wrong[customer::actionform]");
        }
    }
}