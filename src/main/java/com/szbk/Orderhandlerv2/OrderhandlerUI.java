package com.szbk.Orderhandlerv2;

import com.szbk.Orderhandlerv2.view.ContentLayoutForNavigator;
import com.szbk.Orderhandlerv2.view.ErrorView;
import com.szbk.Orderhandlerv2.view.LoginView;
import com.szbk.Orderhandlerv2.view.WelcomeView;
import com.szbk.Orderhandlerv2.view.customerViews.AddOrderView;
import com.szbk.Orderhandlerv2.view.customerViews.CustomerMainView;
import com.szbk.Orderhandlerv2.view.customerViews.CustomerSideBar;
import com.szbk.Orderhandlerv2.view.customerViews.ListOrdersForCustomer;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
//import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@Title("Orderhandler")
@SpringUI
@UIScope
public class OrderhandlerUI extends UI {
    private SpringViewProvider springViewProvider;

//    LoginView loginView;
//    CustomerMainView customerMainView;
    private Navigator navigator;

    private ContentLayoutForNavigator content;

//    public OrderhandlerUI(LoginView loginView, CustomerMainView customerMainView) {
//        this.loginView = loginView;
//        this.customerMainView = customerMainView;
//    }

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
        setContentForUserRole();
    }

    private void setContentForUserRole() {
        String userRole = String.valueOf(VaadinSession.getCurrent().getAttribute("role"));

        if (userRole.equals("customer")) {
            navigator.navigateTo("customerView");
        } else if (userRole.equals("laboruser")) {
//            navigator.navigateTo("laboruserView");
        }
    }
}
