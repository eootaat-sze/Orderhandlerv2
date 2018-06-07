package com.szbk.Orderhandlerv2.view.customerViews;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringComponent
@SpringView(name = "listOrdersForCustomer")
public class ListOrdersForCustomer extends VerticalLayout implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        addComponent(new Label("List is working!"));
    }
}
