package com.szbk.Orderhandlerv2.view.customerViews;

import com.szbk.Orderhandlerv2.model.Entity.CustomerOrder;
import com.vaadin.data.*;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("ALL")
public class SimpleOrderForm extends FormLayout {
    private ComboBox<String> type;
    private TextField sequenceName;
    private TextField sequence;
    private TextField scale;
    private ComboBox<String> purification;
    private Button saveOrderBtn;
    private Button deleteOrderBtn;
    private HorizontalLayout buttonsLayout;

    private Binder<CustomerOrder> dataBinder;

    private CustomerOrder orderToEdit;
    private AddOrderView addOrderView;

    private List<String> typesList;
    private List<String> purificationsList;

    private static String REQUIRED_FIELD = "A mező kitöltése kötelező!";

    public SimpleOrderForm(AddOrderView addOrderView, List<String> typesList, List<String> purificationsList) {
        this.addOrderView = addOrderView;
        this.typesList = typesList;
        this.purificationsList = purificationsList;

        type = new ComboBox<>();
        sequenceName = new TextField();
        sequence = new TextField();
        scale = new TextField();
        purification = new ComboBox<>();
        saveOrderBtn = new Button("Hozzáadás");
        deleteOrderBtn = new Button("Törlés");
        buttonsLayout = new HorizontalLayout(saveOrderBtn, deleteOrderBtn);

        dataBinder = new Binder<>(CustomerOrder.class);

        setupContent();
    }

    private void setupContent() {
        //Main settings.
        setSizeUndefined();

        //Type settings.
        type.setPlaceholder("Szekvencia típusa");
        type.focus();
        type.setEmptySelectionAllowed(false);
        type.setItems(typesList);
//        type.addValueChangeListener(e -> sequence.clear());
        type.addSelectionListener(e -> sequence.clear());
        dataBinder.forField(type).asRequired(REQUIRED_FIELD).bind(CustomerOrder::getType, CustomerOrder::setType);

        //Sequence name settings.
        sequenceName.setPlaceholder("Szekvencia neve");
        dataBinder.forField(sequenceName).asRequired(REQUIRED_FIELD)
                .bind(CustomerOrder::getSequenceName, CustomerOrder::setSequenceName);

        //Sequence settings.
        sequence.setPlaceholder("Szekvencia");
        dataBinder.forField(sequence).asRequired(REQUIRED_FIELD).withValidator(new Validator<String>() {
            @Override
            public ValidationResult apply(String s, ValueContext valueContext) {
                return validateSequenceString(s);
            }
        }).bind(CustomerOrder::getSequence, CustomerOrder::setSequence);

        //Scale settings.
        scale.setPlaceholder("Scale (nmol)");
        dataBinder.forField(scale).asRequired(REQUIRED_FIELD)
                .withConverter(new StringToIntegerConverter("A megadott érték nem egész szám"))
                .bind(CustomerOrder::getScale, CustomerOrder::setScale);

        //Purification settings.
        purification.setPlaceholder("Tisztítás típusa");
        purification.setItems(purificationsList);
        purification.setEmptySelectionAllowed(false);
        dataBinder.forField(purification).asRequired(REQUIRED_FIELD)
                .bind(CustomerOrder::getPurification, CustomerOrder::setPurification);

        //Save order btn settings.
        saveOrderBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveOrderBtn.addClickListener(e -> validateAndAddOrder());

        //Delete order btn settings.
        deleteOrderBtn.addStyleNames(ValoTheme.BUTTON_DANGER, ValoTheme.BUTTON_ICON_ONLY);
        deleteOrderBtn.setIcon(VaadinIcons.CLOSE);
        deleteOrderBtn.setWidth(100, Unit.PERCENTAGE);
        deleteOrderBtn.addClickListener(e -> addOrderView.removeOrderFromGrid(orderToEdit));

        //Buttons layout settings.
        buttonsLayout.setSizeUndefined();

        addComponents(type, sequenceName, sequence, scale, purification, buttonsLayout);
        setVisible(false);
    }

    private void validateAndAddOrder() {
        System.out.println("validateAndAddOrder method in the form class");
        boolean success = false;

        try {
            //If we don't get any exception, add the item to the grid and hide the form.
            dataBinder.writeBean(orderToEdit);
            success = true;
        } catch (ValidationException e) {
            Notification notification = new Notification("Ellenőrizze a hibaüzeneteket az egyes mezők mellett!");
            notification.setStyleName(ValoTheme.NOTIFICATION_ERROR);
            notification.setPosition(Position.TOP_CENTER);
            notification.show(getUI().getPage());
        }

        if (success) {
            orderToEdit.setCustomerId(Long.parseLong(String.valueOf(VaadinSession.getCurrent()
                    .getAttribute("id"))));
            orderToEdit.setOrderDate(LocalDate.now());
            addOrderView.addOrderToTheGrid(orderToEdit);
            setVisible(false);
        }
    }

    private ValidationResult validateSequenceString(String s) {
        if (type.getSelectedItem().isPresent()) {
            String selectedType = type.getSelectedItem().get();

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

    /**
     *
     * @param order
     * @param newOrder if true, the incoming order is a new one, so we cannot delete it,
     *                 so the delete button will be disabled; if false we can delete the order with the button.
     */
    public void setOrderToEdit(CustomerOrder order, boolean newOrder) {
        deleteOrderBtn.setEnabled(!newOrder);

        if (order != null) {
            this.orderToEdit = order;
            dataBinder.setBean(orderToEdit);
            setVisible(true);
        } else {
            System.out.println("Order is null");
        }
    }
}
