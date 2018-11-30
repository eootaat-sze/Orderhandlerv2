package com.szbk.Orderhandlerv2.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.CustomComponent;

public class ErrorView extends CustomComponent implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        System.out.println("Something went wrong!");
        System.out.println("[erroview] user role: " + VaadinSession.getCurrent().getAttribute("role"));
        VaadinSession.getCurrent().setAttribute("role", "none");
        getUI().getNavigator().navigateTo("");
    }
}
