package com.szbk.Orderhandlerv2.view.customerViews;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
//@SpringView(name = "customerView")
public class CustomerMainView extends HorizontalLayout implements View {
    private CssLayout sideMenu;
    private CssLayout contentView;

    @Autowired
    public CustomerMainView() {
        sideMenu = new CustomerSideBar();
        contentView = new CssLayout();

        setupContent();
    }

    private void setupContent() {
        //Class level settings.
        setSizeFull();
        addComponents(sideMenu, contentView);
    }

    public CssLayout getContentView() {
        return contentView;
    }
}
