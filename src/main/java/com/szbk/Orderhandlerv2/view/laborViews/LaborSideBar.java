package com.szbk.Orderhandlerv2.view.laborViews;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@SpringView(name = "laboruser")
public class LaborSideBar extends CssLayout implements View {
    private Label menuTitle;
    private Button allOrdersBtn;
    private Button createReportBtn;
    private Button createSettlementBtn;
    private Button overviewBtn;
    private Button logoutBtn;

    public LaborSideBar() {
        menuTitle = new Label("Hello");
        allOrdersBtn = new Button("Összes megrendelés");
        createReportBtn = new Button("Report készítés");
        createSettlementBtn = new Button("Elszámolás készítése");
        overviewBtn = new Button("Áttekintő nézet");
        logoutBtn = new Button("Kilépés");

        setupContent();
    }

    private void setupContent() {
        setSizeUndefined();

        //Title label settings.
        menuTitle.setStyleName(ValoTheme.MENU_TITLE);

        //All orders btn settings.
        allOrdersBtn.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        allOrdersBtn.setWidth(100, Unit.PERCENTAGE);
        allOrdersBtn.setIcon(VaadinIcons.LIST);
        allOrdersBtn.addClickListener(e -> getUI().getNavigator().navigateTo("ordersView"));

        //Create report btn settings.
        createReportBtn.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        createReportBtn.setWidth(100, Unit.PERCENTAGE);
        createReportBtn.setIcon(VaadinIcons.PRINT);
        createReportBtn.addClickListener(e -> getUI().getNavigator().navigateTo("createReport"));

        //Create settlement btn settings.
        createSettlementBtn.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        createSettlementBtn.setWidth(100, Unit.PERCENTAGE);
        createSettlementBtn.setIcon(VaadinIcons.CREDIT_CARD);
        createSettlementBtn.addClickListener(e -> getUI().getNavigator().navigateTo("settlementView"));

        //Overview btn settings.
        overviewBtn.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        overviewBtn.setWidth(100, Unit.PERCENTAGE);
        overviewBtn.setIcon(VaadinIcons.DATABASE);
        overviewBtn.addClickListener(e -> Notification.show("Yeey!"));

        //Logout button settings.
        logoutBtn.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        logoutBtn.setWidth(100, Unit.PERCENTAGE);
        logoutBtn.setIcon(VaadinIcons.EXIT);
        logoutBtn.addClickListener(e -> {
//            getUI().getPage().reload();
            VaadinSession.getCurrent().setAttribute("role", "none");
            getUI().getNavigator().navigateTo("");
        });

        addComponents(menuTitle, allOrdersBtn, createReportBtn, createSettlementBtn, overviewBtn, logoutBtn);
        setStyleName(ValoTheme.MENU_ROOT);
    }
    
    @Override
    public void enter(ViewChangeEvent event) {
        View.super.enter(event);
    }
}