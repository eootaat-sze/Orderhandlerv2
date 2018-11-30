package com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents.Forms;

import com.szbk.Orderhandlerv2.controller.OrderController;
import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.szbk.Orderhandlerv2.view.laborViews.OverviewComponents.CustomerOrderComponent;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.AbstractField;

public class OrderActionForm extends VerticalLayout {
    private CustomerOrderComponent view;
    private OrderController controller;
    private CustomerOrder orderToEdit;

    private Label formTitle;
    private ComboBox<String> customerSelect;
    private ComboBox<String> typeSelect;
    private TextField sequenceNameField;
    private TextField sequenceField;
    private TextField editedSequenceField;
    private TextField purificationField;
    private TextField scaleField;
    private Button saveBtn;
    private Binder<CustomerOrder> dataBinder;

    private static final String TYPECHANGE_MESSAGE = "Változott a típus, ellenőrizd a szekvenciát!";

    public OrderActionForm(CustomerOrderComponent view, OrderController controller) {
        this.view = view;
        this.controller = controller;

        formTitle = new Label("Hozzáadás/módosítás");
        customerSelect = new ComboBox<>();
        typeSelect = new ComboBox<>();
        sequenceNameField = new TextField();
        sequenceField = new TextField();
        editedSequenceField = new TextField();
        purificationField = new TextField();
        scaleField = new TextField();
        saveBtn = new Button("Mentés");
        dataBinder = new Binder<>(CustomerOrder.class);

        setupContent();
    }

    private void setupContent() {
        //View settings
        setMargin(false);
        addComponents(formTitle, customerSelect, typeSelect, sequenceNameField, sequenceField, editedSequenceField, purificationField, scaleField, saveBtn);
        setComponentAlignment(saveBtn, Alignment.BOTTOM_CENTER);

        setWidthForComponents(this, formTitle, customerSelect, typeSelect, sequenceNameField, sequenceField, editedSequenceField, purificationField, scaleField);

        //Formtitle settings
        formTitle.addStyleNames(ValoTheme.LABEL_H3, ValoTheme.TEXTAREA_ALIGN_CENTER);

        //Customer select settings
        customerSelect.setPlaceholder("Megrendelő kiválasztása");
        customerSelect.setDescription("Megrendelő kiválasztása");
        customerSelect.setItems("1: Valaki", "2: Valaki", "3: Valaki");
        customerSelect.setEmptySelectionAllowed(false);
        customerSelect.setRequiredIndicatorVisible(true);
        customerSelect.addValueChangeListener(event -> {
            if (!event.getValue().isEmpty()) {
                saveBtn.setEnabled(true);
            }
        });

        typeSelect.setPlaceholder("Típus kiválasztása");
        typeSelect.setDescription("Típus kiválasztása");
        typeSelect.setItems("DNS", "RNS", "Módosított DNS", "Módosított RNS");
        typeSelect.setEmptySelectionAllowed(false);
        typeSelect.addSelectionListener(event -> showNotificationOnTypeChange());
        dataBinder.forField(typeSelect).asRequired("Kötelező a megadása!").bind(CustomerOrder::getType, CustomerOrder::setType);
        
        setPlaceholderAndDescriptionForTextFields(sequenceNameField, "Szekvencia neve");
        dataBinder.forField(sequenceNameField).asRequired().bind(CustomerOrder::getSequenceName, CustomerOrder::setSequenceName);

        setPlaceholderAndDescriptionForTextFields(sequenceField, "Szekvencia");
        dataBinder.forField(sequenceField).asRequired().withValidator(new Validator<String>() {
			@Override
			public ValidationResult apply(String value, ValueContext context) {
				return validateSequenceString(value);
			}
        }).bind(CustomerOrder::getSequence, CustomerOrder::setSequence);

        setPlaceholderAndDescriptionForTextFields(editedSequenceField, "Szerkesztett szekvencia");
        editedSequenceField.setEnabled(false);
        dataBinder.forField(editedSequenceField).bind(CustomerOrder::getEditedSequence, CustomerOrder::setEditedSequence);

        setPlaceholderAndDescriptionForTextFields(purificationField, "Tisztítás");
        dataBinder.forField(purificationField).asRequired().bind(CustomerOrder::getPurification, CustomerOrder::setPurification);

        setPlaceholderAndDescriptionForTextFields(scaleField, "Szintézis skála");
        dataBinder.forField(scaleField).asRequired().withConverter(
            new StringToIntegerConverter("A megadott érték nem egész szám!")
        ).bind(CustomerOrder::getScale, CustomerOrder::setScale);

        saveBtn.addStyleNames(ValoTheme.BUTTON_PRIMARY);
        saveBtn.setWidth(50, Unit.PERCENTAGE);
        saveBtn.setEnabled(false);
        saveBtn.addClickListener(event -> validateAndSaveOrder());
    }

    private void showNotificationOnTypeChange() {
        Notification notification = new Notification(TYPECHANGE_MESSAGE);
        notification.setStyleName(ValoTheme.NOTIFICATION_WARNING);
        notification.setPosition(Position.TOP_RIGHT);
        notification.show(getUI().getPage());
    }

    private void validateAndSaveOrder() {
        try {
            dataBinder.writeBean(orderToEdit);
        } catch (ValidationException e) {
            System.out.println("[orderactionform] Something is not valid!");
        }
    }

    private void setPlaceholderAndDescriptionForTextFields(TextField field, String text) {
        field.setPlaceholder(text);
        field.setDescription(text);
    }

    public void setOrderToEdit(CustomerOrder order) {
        if (order != null) {
            orderToEdit = order;
            dataBinder.setBean(orderToEdit);
            setVisible(true);
        } else {
            System.out.println("Something went wrong[order::actionform]");
        }
    }

    private void setWidthForComponents(Component... components) {
        for (Component c: components) {
            c.setWidth(100, Unit.PERCENTAGE);
        }
    }

    private ValidationResult validateSequenceString(String s) {
        if (typeSelect.getSelectedItem().isPresent()) {
            String selectedType = typeSelect.getSelectedItem().get();

            if (selectedType.equals("DNS")) {
                s = s.replace("A", "x").replace("C", "x")
                        .replace("G", "x").replace("T", "x");

                System.out.println("sequence: " + s);

                if (s.matches("x{" + s.length() + "}")) {
                    System.out.println("True");
                    return ValidationResult.ok();
                } else {
                    System.out.println("False");
                    return ValidationResult.error("A formátum nem illeszkedik a kiválasztott típushoz!");
                }
            } else if (selectedType.equals("RNS")) {
                s = s.replace("A", "x").replace("C", "x")
                        .replace("G", "x").replace("U", "x");

                if (s.matches("x{" + s.length() + "}")) {
                    System.out.println("True");
                    return ValidationResult.ok();
                } else {
                    System.out.println("False");
                    return ValidationResult.error("A formátum nem illeszkedik a kiválasztott típushoz!");
                }
            }
        }

        return ValidationResult.ok();
    }
}