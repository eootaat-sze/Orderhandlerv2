package com.szbk.Orderhandlerv2.view;

import java.io.File;

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
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
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
    private LaborUserController laborUserController;
    private CustomerController customerController;

    private Panel contentPanel;
    private HorizontalLayout loginLayout;
    private VerticalLayout mainLayout;
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginBtn;
    private Button registrationBtn;
    private HorizontalLayout outerLayout;
    private Image img;

    private Binder<User> dataBinder;
    private String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

    private static String REQUIRED_FIELD = "A mező kitöltése kötelező!";
    private static String INVALID_EMAIL = "Érvénytelen email cím formátum!";

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
        outerLayout = new HorizontalLayout();
        dataBinder = new Binder<>();

        // System.out.println("[loginview] constructor");
    }

   @Override
   public void enter(ViewChangeListener.ViewChangeEvent event) {
    //    System.out.println("[loginview] enter method");
       setContentForUserRole();
   }

    private void setupContent() {
        System.out.println("path: " + basepath);
        // System.out.println("[loginview] setupcontent method");
        setSizeFull();

        //Img settings.
        FileResource resource = new FileResource(new File(basepath + "/WEB-INF/images/background.jpg"));
        img = new Image("", resource);
        img.setWidth(100, Unit.PERCENTAGE);
        img.setHeight(100, Unit.PERCENTAGE);

        //Email field settings
        emailField.setCaption("Email cím");
        emailField.setWidth(100, Unit.PERCENTAGE);
        emailField.setPlaceholder("Email cím");
        emailField.setIcon(VaadinIcons.USER);
        emailField.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        emailField.focus();
        dataBinder.forField(emailField)
                .withValidator(new EmailValidator(INVALID_EMAIL))
                .bind(User::getEmail, User::setEmail);

        //Password field settings
        passwordField.setCaption("Jelszó");
        passwordField.setWidth(100, Unit.PERCENTAGE);
        passwordField.setPlaceholder("Jelszó");
        passwordField.setIcon(VaadinIcons.LOCK);
        passwordField.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        dataBinder.forField(passwordField).asRequired(REQUIRED_FIELD)
                .bind(User::getPassword, User::setPassword);

        //Login button settings
        loginBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        loginBtn.setWidth(50, Unit.PERCENTAGE);
        loginBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginBtn.addClickListener(e -> {
            validateAndLoginUser();
        });

        //Registration button settings
        registrationBtn.addClickListener(e -> {
            //Navigate to the registration view, or open registration window. It depends on... idk. update: Navigator FTW!
            getUI().getNavigator().navigateTo("registration");
        });

        // loginLayout.addComponents(emailField, passwordField, loginBtn);
        // mainLayout.addComponents(loginLayout, registrationBtn);
        mainLayout.addComponents(emailField, passwordField, loginBtn, registrationBtn);
        mainLayout.setComponentAlignment(loginBtn, Alignment.BOTTOM_CENTER);
        mainLayout.setComponentAlignment(registrationBtn, Alignment.BOTTOM_CENTER);

        //Content panel settings.
        // contentPanel.setSizeUndefined();
        contentPanel.setWidth(100, Unit.PERCENTAGE);
        contentPanel.setCaption("Belépés");
        contentPanel.setContent(mainLayout);

        outerLayout.setWidth(50, Unit.PERCENTAGE);
        outerLayout.setHeight(50, Unit.PERCENTAGE);
        // outerLayout.addComponents(img, contentPanel);
        outerLayout.addComponents(contentPanel);
        // outerLayout.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
        // outerLayout.setComponentAlignment(contentPanel, Alignment.MIDDLE_RIGHT);

        addComponents(outerLayout);
        setComponentAlignment(outerLayout, Alignment.MIDDLE_CENTER);
    }

    private void validateAndLoginUser() {
        User loginUser = new User();
        boolean validationSuccess = false;

        try {
            dataBinder.writeBean(loginUser);
            validationSuccess = true;
        } catch(ValidationException e) {
            Notification notification = new Notification("Ellenőrizze a hibaüzeneteket az egyes mezők mellett!");
            notification.setStyleName(ValoTheme.NOTIFICATION_ERROR);
            notification.setPosition(Position.TOP_CENTER);
            notification.show(getUI().getPage());
        }

        //Basic idea: try to login a laboruser and if that fails, try to login a customer.
        //It's an efficient way, we can say, because there'll be less laboruser than customer, so it worth to search through
        //their (laboruser) table first.
        if (validationSuccess) {
            LaborUser laboruserToLogin = laborUserController.login(loginUser.getEmail(), loginUser.getPassword());
            System.out.println("laboruser[login]: " + laboruserToLogin);
            if (laboruserToLogin != null) {
                VaadinSession.getCurrent().setAttribute("laborUsername", laboruserToLogin.getName());
                VaadinSession.getCurrent().setAttribute("email", laboruserToLogin.getEmail());
                VaadinSession.getCurrent().setAttribute("role", "laboruser");
                getUI().getNavigator().navigateTo("laboruser");
            } else {
                Customer customerToLogin = customerController.login(loginUser.getEmail(), loginUser.getPassword());
                System.out.println("customer[login]: " + customerToLogin);
                if (customerToLogin != null) {
                    VaadinSession.getCurrent().setAttribute("customerName", customerToLogin.getCustomerName());
                    VaadinSession.getCurrent().setAttribute("groupName", customerToLogin.getGroupName());
                    VaadinSession.getCurrent().setAttribute("companyName", customerToLogin.getCompanyName());
                    VaadinSession.getCurrent().setAttribute("id", customerToLogin.getId());
                    VaadinSession.getCurrent().setAttribute("role", "customer");
                    VaadinSession.getCurrent().setAttribute("innerName", customerToLogin.getInnerName());
                    getUI().getNavigator().navigateTo("customer");
                } else {
                    //No user with the specified data, display a notification.
                    Notification notification = new Notification("Sikertelen belépés! Nincs ilyen felhasználó!");
                    notification.setStyleName(ValoTheme.NOTIFICATION_ERROR);
                    notification.setPosition(Position.TOP_CENTER);
                    notification.show(getUI().getPage());
                }
            }
        }
    }

    //Routing by role: if the role is neither customer nor laboruser, setup the login view content.
    private void setContentForUserRole() {
        String userRole = String.valueOf(VaadinSession.getCurrent().getAttribute("role"));

        if (userRole.equals("customer")) {
            getUI().getNavigator().navigateTo("customerView");
        } else if (userRole.equals("laboruser")) {
           getUI().getNavigator().navigateTo("laboruserView");
        } else {
            setupContent();
        }
    }
}
