package com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents;

import com.szbk.Orderhandlerv2.controller.LaborUserController;
import com.szbk.Orderhandlerv2.model.Entity.LaborUser;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class LaboruserActionForm extends VerticalLayout {
    private LaborUserController laborUserController;
    private LaboruserComponent view;

    private Label formTitle;
    private TextField name;
    private TextField email;
    private PasswordField password;
    private Button saveBtn;
    private Binder<LaborUser> dataBinder;
    private LaborUser userToEdit;

    public LaboruserActionForm(LaboruserComponent view, LaborUserController laborUserController) {
        this.laborUserController = laborUserController;
        this.view = view;

        formTitle = new Label("Hozzáadás/módosítás");
        name = new TextField();
        email = new TextField();
        password = new PasswordField();
        saveBtn = new Button("Mentés");
        dataBinder = new Binder<>(LaborUser.class);

        setupContent();
    }

    private void setupContent() {
        //View settings
        setSizeUndefined();
        setMargin(false);
        addComponents(formTitle, name, email, password, saveBtn);
        setComponentAlignment(formTitle, Alignment.TOP_CENTER);
        setComponentAlignment(saveBtn, Alignment.BOTTOM_CENTER);
        addAttachListener(event -> System.out.println("Form attached"));

        //Form title settings
        formTitle.addStyleNames(ValoTheme.LABEL_BOLD);

        //Name field settings
        name.setPlaceholder("Felhasználó neve");
        name.setWidth(100, Unit.PERCENTAGE);
        name.focus();
        dataBinder.forField(name).asRequired().bind(LaborUser::getName, LaborUser::setName);

        //Email field settings
        email.setPlaceholder("Email cím");
        dataBinder.forField(email).asRequired().withValidator(new EmailValidator("Ez nem valid email cím!"))
                .bind(LaborUser::getEmail, LaborUser::setEmail);

        //Password field settings
        password.setPlaceholder("Jelszó");
        dataBinder.forField(password).asRequired().bind(LaborUser::getPassword, LaborUser::setPassword);

        //Save btn settings
        saveBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        saveBtn.addClickListener(e -> validateAndSaveLaboruser());

        setVisible(false);
    }

	private void validateAndSaveLaboruser() {
		try {
            dataBinder.writeBean(userToEdit);
            
            if (laborUserController.saveLaboruser(userToEdit)) {
                view.updateGrid();
                setVisible(false);
                //Maybe a notification here to show, which says the action was successfull?
            }
        } catch (ValidationException e) {
            Notification notification = new Notification("Ellenőrizd a hibaüzeneteket az egyes mezők mellett!");
            notification.setStyleName(ValoTheme.NOTIFICATION_ERROR);
            notification.setPosition(Position.TOP_CENTER);
            notification.show(getUI().getPage());
        }
    }
    
    public void setUserToEdit(LaborUser user) {
        if (user != null) {
            userToEdit = user;
            dataBinder.setBean(userToEdit);
            setVisible(true);
        } else {
            System.out.println("Something went wrong[laboruser::actionform]");
        }
    }
}