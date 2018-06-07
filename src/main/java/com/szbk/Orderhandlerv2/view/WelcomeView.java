package com.szbk.Orderhandlerv2.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;

public class WelcomeView extends VerticalLayout implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        System.out.println("Hello");
    }
}
