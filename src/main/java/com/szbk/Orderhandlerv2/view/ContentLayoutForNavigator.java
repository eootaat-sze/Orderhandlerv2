package com.szbk.Orderhandlerv2.view;

import com.szbk.Orderhandlerv2.view.customerViews.CustomerSideBar;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.CssLayout;

public class ContentLayoutForNavigator extends CssLayout implements ViewDisplay {
    private CssLayout menuLayout;
    private CssLayout contentLayout;

    public ContentLayoutForNavigator() {
        setSizeFull();

        menuLayout = new CssLayout();
        contentLayout = new CssLayout();
        contentLayout.setSizeFull();

//        addComponents(menuLayout, contentLayout);
        addComponent(contentLayout);
    }

    @Override
    public void showView(View view) {
        if (view instanceof CustomerSideBar) {
            addComponentAsFirst(menuLayout);
            menuLayout.setHeight(100, Unit.PERCENTAGE);
            menuLayout.setWidth(20, Unit.PERCENTAGE);
            menuLayout.addComponent(view.getViewComponent());
            contentLayout.removeAllComponents();
            contentLayout.setWidth(80, Unit.PERCENTAGE);
            contentLayout.setHeight(100, Unit.PERCENTAGE);
            System.out.println("sidebar attach");
        } else if (view instanceof LoginView) {
//            menuLayout.removeAllComponents();
            removeComponent(menuLayout);
            contentLayout.removeAllComponents();
            contentLayout.setSizeFull();
            contentLayout.addComponent(view.getViewComponent());
        } else {
            contentLayout.removeAllComponents();
            contentLayout.addComponent(view.getViewComponent());
            System.out.println("content attach, " + view.getViewComponent());
        }
    }
}
