package com.szbk.Orderhandlerv2.view;

import com.szbk.Orderhandlerv2.controller.CustomerController;
import com.szbk.Orderhandlerv2.model.Entity.Customer;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("Duplicates")
@SpringComponent
@SpringView(name = "registration")
public class RegistrationView extends VerticalLayout implements View {
    private CustomerController customerController;

    private Panel contentPanel;
    private VerticalLayout mainLayout;
    private TextField customerNameField;
    private PasswordField passwordField;
    private PasswordField password2Field;
    private TextField emailField;
    private TextField groupNameField;
    private TextField companyNameField;
    private Button registrationBtn;
    private Button cancelBtn;

    private Binder<Customer> dataBinder;

    private static String REQUIRED_FIELD = "A mező kitöltése kötelező!";

    @Autowired
    public RegistrationView(CustomerController customerController) {
        this.customerController = customerController;

        contentPanel = new Panel();
        mainLayout = new VerticalLayout();
        customerNameField = new TextField();
        passwordField = new PasswordField();
        password2Field = new PasswordField();
        emailField = new TextField();
        groupNameField = new TextField();
        companyNameField = new TextField();
        registrationBtn = new Button("Regisztrálok");
        cancelBtn = new Button("Mégse");

        dataBinder = new Binder<>();

        setupContent();
    }

    private void setupContent() {
        setSizeFull();

        //Customername field settings.
        customerNameField.focus();
        customerNameField.setPlaceholder("Megrendelő neve");
        customerNameField.setDescription("Adja meg a megrendelő személy nevét");
        customerNameField.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(customerNameField).asRequired(REQUIRED_FIELD)
                .bind(Customer::getCustomerName, Customer::setCustomerName);

        //Password field settings.
        passwordField.setPlaceholder("Jelszó");
        passwordField.setDescription("Adja meg a jelszavát!");
        passwordField.setWidth(100, Unit.PERCENTAGE);
        //I don't bind it here, because at this moment I don't know they are equal or not. And they should.
        dataBinder.forField(passwordField).asRequired(REQUIRED_FIELD)
                .bind(Customer::getPassword, Customer::setPassword);

        //Password2 field settings.
        password2Field.setPlaceholder("Jelszó megint");
        password2Field.setDescription("Adja meg a jelszavát megint!");
        password2Field.setWidth(100, Unit.PERCENTAGE);

        //Email field settings.
        emailField.setPlaceholder("Email address");
        emailField.setDescription("Adja meg az emailField címét!");
        emailField.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(emailField).asRequired("Email megadása kötelező!")
                .withValidator(new EmailValidator("Ez nem valid emailField cím!"))
                .bind(Customer::getEmail, Customer::setEmail);

        //Groupname field settings.
        groupNameField.setPlaceholder("Csoportnév");
        groupNameField.setDescription("Adja meg a csoport nevét!");
        groupNameField.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(groupNameField).asRequired(REQUIRED_FIELD)
                .bind(Customer::getGroupName, Customer::setGroupName);

        //Companyname field settings.
        companyNameField.setPlaceholder("Cégnév");
        companyNameField.setDescription("Adja meg a cég nevét!");
        companyNameField.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(companyNameField).asRequired(REQUIRED_FIELD)
                .bind(Customer::getCompanyName, Customer::setCompanyName);

        //Registration button settings.
        registrationBtn.setDescription("Erre a gombra klikkelve regisztrálhat a rendszerbe.");
        registrationBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        registrationBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        registrationBtn.addClickListener(e -> {
            validateAndRegisterCustomer();
        });

        //Cancel button settings.
        cancelBtn.setDescription("Regisztráció megszakítása");
        cancelBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
        cancelBtn.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        cancelBtn.addClickListener(e -> getUI().getNavigator().navigateTo(""));

        //Main layout settings.
        mainLayout.addComponents(customerNameField, passwordField, password2Field, emailField, groupNameField,
                companyNameField, registrationBtn, cancelBtn);
        mainLayout.setComponentAlignment(registrationBtn, Alignment.BOTTOM_CENTER);
        mainLayout.setComponentAlignment(cancelBtn, Alignment.BOTTOM_CENTER);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        mainLayout.setWidth(300, Unit.PIXELS);

        //Content panel settings.
        contentPanel.setSizeUndefined();
        contentPanel.setCaption("Regisztráció");
        contentPanel.setContent(mainLayout);
        addComponent(contentPanel);
        setComponentAlignment(contentPanel, Alignment.MIDDLE_CENTER);
    }

    private void validateAndRegisterCustomer() {
        Customer customer = new Customer();
        boolean validationSuccess = true;
        
        //The two passwords has to be equal.
        if (passwordField.getValue().equals(password2Field.getValue())) {
            if (checkEmail()) {
                try {
                    dataBinder.writeBean(customer);
                    System.out.println("Customer bean: " + customer);
                } catch (ValidationException e) {
                    Notification errorMessage = new Notification("Ellenőrizze a hibaüzeneteket az egyes mezők mellett!");
                    errorMessage.setStyleName(ValoTheme.NOTIFICATION_ERROR);
                    errorMessage.setPosition(Position.TOP_CENTER);
                    errorMessage.show(getUI().getPage());
                    validationSuccess = false;
                }
            } else {
                Notification invalidEmailWarning = new Notification("Az email cím már foglalt!");
                invalidEmailWarning.setStyleName(ValoTheme.NOTIFICATION_WARNING);
                invalidEmailWarning.setPosition(Position.TOP_CENTER);
                invalidEmailWarning.show(getUI().getPage());
                validationSuccess = false;
            }
        } else {
            Notification passwordNotEqualWarning = new Notification("A két jelszó nem egyezik!");
            passwordNotEqualWarning.setStyleName(ValoTheme.NOTIFICATION_WARNING);
            passwordNotEqualWarning.setPosition(Position.TOP_CENTER);
            passwordNotEqualWarning.show(getUI().getPage());
            validationSuccess = false;
        }

        if (validationSuccess) {
            Notification notification = new Notification("Sikeres regisztráció!");
            notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
            notification.setPosition(Position.TOP_CENTER);

            if (customerController.registration(customer)) {
                //TODO Maybe a duration to the success notification to not disappear that fast?
                notification.show(getUI().getPage());
                getUI().getNavigator().navigateTo("");
            } else {
                notification.setStyleName(ValoTheme.NOTIFICATION_WARNING);
                notification.setCaption("Sikertelen regisztráció!");
                notification.show(getUI().getPage());
            }
        }
    }

    //Checks, whether someone else registered with the given email before.
    private boolean checkEmail() {
        String emailAddress = emailField.getValue();
        return customerController.getCustomerByEmail(emailAddress) == null;
    }
}
