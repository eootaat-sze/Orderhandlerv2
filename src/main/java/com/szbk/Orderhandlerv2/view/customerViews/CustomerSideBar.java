package com.szbk.Orderhandlerv2.view.customerViews;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@SpringView(name = "customer")
public class CustomerSideBar extends CssLayout implements View {
    private Label menuTitle;
    private Button addOrderBtn;
    private Button listOrdersBtn;
    private Button logoutBtn;

    public CustomerSideBar() {
        menuTitle = new Label("Hello");
        addOrderBtn = new Button("Rendelés leadása");
        listOrdersBtn = new Button("Eddigi rendeléseim");
        logoutBtn = new Button("Kilépés");

        // setupContent();
    }

    private void setupContent() {
        setSizeUndefined();

        //Title label settings.
        menuTitle.setStyleName(ValoTheme.MENU_TITLE);

        //Add order button settings.
        addOrderBtn.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        addOrderBtn.setWidth(100, Unit.PERCENTAGE);
        addOrderBtn.setIcon(VaadinIcons.CART);
        addOrderBtn.addClickListener(e -> getUI().getNavigator().navigateTo("addOrder"));

        //List orders button settings.
        listOrdersBtn.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        listOrdersBtn.setWidth(100, Unit.PERCENTAGE);
        listOrdersBtn.setIcon(VaadinIcons.LIST);
        listOrdersBtn.addClickListener(e -> getUI().getNavigator().navigateTo("ordersListView"));

        //Logout button settings.
        logoutBtn.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        logoutBtn.setWidth(100, Unit.PERCENTAGE);
        logoutBtn.setIcon(VaadinIcons.EXIT);
        logoutBtn.addClickListener(e -> {
//            getUI().getPage().reload();
            VaadinSession.getCurrent().setAttribute("role", null);
            getUI().getNavigator().navigateTo("");
            // VaadinSession.getCurrent().close();
            // getUI().getPage().setLocation("localhost:8080");
        });

        addComponents(menuTitle, addOrderBtn, listOrdersBtn, logoutBtn);
        setStyleName(ValoTheme.MENU_ROOT);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        String role = String.valueOf(VaadinSession.getCurrent().getAttribute("role"));
        System.out.println("customer_role: " + role);
        if (role == null || !role.equals("customer")) {
            getUI().getNavigator().navigateTo("");
            System.out.println("No session");
        }

        setupContent();
    }
}
