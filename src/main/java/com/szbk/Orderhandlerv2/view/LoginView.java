package com.szbk.Orderhandlerv2.view;

import com.szbk.Orderhandlerv2.controller.CustomerController;
import com.szbk.Orderhandlerv2.controller.LaborUserController;
import com.szbk.Orderhandlerv2.model.Entity.Customer;
import com.szbk.Orderhandlerv2.model.Entity.LaborUser;
import com.szbk.Orderhandlerv2.model.Entity.User;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@SpringView(name = "")
public class LoginView extends VerticalLayout implements View {
//    @Autowired
    private LaborUserController laborUserController;

//    @Autowired
    private CustomerController customerController;

    private Panel contentPanel;
    private HorizontalLayout loginLayout;
    private VerticalLayout mainLayout;
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginBtn;
    private Button registrationBtn;
    private Notification popupInformation;

    private Binder<User> dataBinder;

    private static String REQUIRED_FIELD = "A mező kitöltése kötelező!";

    public LoginView() {}

    @Autowired
    public LoginView(LaborUserController lbController, CustomerController customerController) {
        this.laborUserController = lbController;
        this.customerController = customerController;

        contentPanel = new Panel();
        loginLayout = new HorizontalLayout();
        mainLayout = new VerticalLayout();
        emailField = new TextField();
        passwordField = new PasswordField();
        loginBtn = new Button("Belépés");
        registrationBtn = new Button("Regisztráció");
        popupInformation = new Notification("Információ");

        dataBinder = new Binder<>();

        setupContent();
    }

//    @Override
//    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        setupContent();
//    }

    private void setupContent() {
        setSizeFull();

        //Displays a notification with some information, but I didn't work it out properly.
    //    popupInformation.setStyleName(ValoTheme.NOTIFICATION_CLOSABLE);
    //    popupInformation.setDescription("Ez itt a főoldal. Itt tudsz belépni vagy regisztrálni.");
    //    popupInformation.setDelayMsec(-1);
    //    popupInformation.setPosition(Position.TOP_RIGHT);
    //    popupInformation.setIcon(VaadinIcons.INFO);
    //    popupInformation.show(UI.getCurrent().getPage());

        //Email field settings
        emailField.setPlaceholder("Email cím");
        emailField.setIcon(VaadinIcons.USER);
        emailField.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        emailField.focus();
//        dataBinder.forField(emailField).asRequired(REQUIRED_FIELD)
//                .withValidator(new EmailValidator("Érvénytelen email cím formátum!"))
//                .bind(User::getEmail, User::setEmail);
        dataBinder.forField(emailField)
                .withValidator(new EmailValidator("Érvénytelen email cím formátum!"))
                .bind(User::getEmail, User::setEmail);

        //Password field settings
        passwordField.setPlaceholder("Jelszó");
        passwordField.setIcon(VaadinIcons.LOCK);
        passwordField.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
//        dataBinder.forField(passwordField).asRequired(REQUIRED_FIELD).bind(User::getPassword, User::setPassword);
        dataBinder.forField(passwordField).bind(User::getPassword, User::setPassword);

        //Login button settings
        loginBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        loginBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginBtn.addClickListener(e -> {
            validateAndLoginUser();
            // getUI().getNavigator().navigateTo("laboruser");
        });

        //Registration button settings
        registrationBtn.addClickListener(e -> {
            //Navigate to the registration view, or open registration window. It depends on... idk.
//            showRegistrationWindow();
            // getUI().getNavigator().navigateTo("registration");
            getUI().getNavigator().navigateTo("laboruser");
        });

        loginLayout.addComponents(emailField, passwordField, loginBtn);
        mainLayout.addComponents(loginLayout, registrationBtn);
        mainLayout.setComponentAlignment(registrationBtn, Alignment.BOTTOM_CENTER);

        //Content panel settings.
        contentPanel.setSizeUndefined();
        contentPanel.setCaption("Belépés");
        contentPanel.setContent(mainLayout);

        addComponents(contentPanel);
        setComponentAlignment(contentPanel, Alignment.MIDDLE_CENTER);
    }

    private void validateAndLoginUser() {
        User loginUser = new User();
        boolean validationSuccess = false;

        try {
            dataBinder.writeBean(loginUser);
//            System.out.println("user: " + loginUser);
            validationSuccess = true;
        } catch(ValidationException e) {
            Notification notification = new Notification("Ellenőrizze a hibaüzeneteket az egyes mezők mellett!");
            notification.setStyleName(ValoTheme.NOTIFICATION_ERROR);
            notification.setPosition(Position.TOP_CENTER);
            notification.show(getUI().getPage());
        }

        if (validationSuccess) {
            LaborUser laboruserToLogin = laborUserController.login(loginUser.getEmail(), loginUser.getPassword());

            if (laboruserToLogin != null) {
                VaadinSession.getCurrent().setAttribute("laborUsername", laboruserToLogin.getName());
                VaadinSession.getCurrent().setAttribute("email", laboruserToLogin.getEmail());
                VaadinSession.getCurrent().setAttribute("role", "laboruser");
//                VaadinSession.getCurrent().setAttribute("id", lb.getId());
                getUI().getNavigator().navigateTo("laboruser");
            } else {
                Customer customerToLogin = customerController.login(loginUser.getEmail(), loginUser.getPassword());
                if (customerToLogin != null) {
                    VaadinSession.getCurrent().setAttribute("customerName", customerToLogin.getCustomerName());
                    VaadinSession.getCurrent().setAttribute("groupName", customerToLogin.getGroupName());
                    VaadinSession.getCurrent().setAttribute("companyName", customerToLogin.getCompanyName());
                    VaadinSession.getCurrent().setAttribute("id", customerToLogin.getId());
                    VaadinSession.getCurrent().setAttribute("role", "customer");
                    VaadinSession.getCurrent().setAttribute("innerName", customerToLogin.getInnerName());
                    getUI().getNavigator().navigateTo("customer");
                }
            }

            //No user with the specified data, display a notification.
            Notification notification = new Notification("Sikertelen belépés! Nincs ilyen felhasználó!");
            notification.setStyleName(ValoTheme.NOTIFICATION_ERROR);
            notification.setPosition(Position.TOP_CENTER);
        }
    }

    private void showRegistrationWindow() {
        getUI().getNavigator().navigateTo("registration");
    }
}
