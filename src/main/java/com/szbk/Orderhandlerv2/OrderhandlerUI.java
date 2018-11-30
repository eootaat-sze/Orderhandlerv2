package com.szbk.Orderhandlerv2;

import com.szbk.Orderhandlerv2.view.ContentLayoutForNavigator;
import com.szbk.Orderhandlerv2.view.ErrorView;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@Title("Orderhandler")
@SpringUI
@UIScope
@PushStateNavigation
public class OrderhandlerUI extends UI {
    private SpringViewProvider springViewProvider;
    
    private Navigator navigator;

    private ContentLayoutForNavigator content;

    @Autowired
    public OrderhandlerUI(SpringViewProvider springViewProvider) {
        this.springViewProvider = springViewProvider;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        content = new ContentLayoutForNavigator();

        navigator = new Navigator( this, (ViewDisplay) content);
        navigator.addProvider(springViewProvider);
        //Handle errors.
        navigator.setErrorView(ErrorView.class);
        setContent(content);
        System.out.println("[orderhandlerUI] init method");
        // setContentForUserRole();
    }
}
