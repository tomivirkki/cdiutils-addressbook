package org.vaadin.virkki.cdiutils.addressbook;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.application.UIContext.ContextDelegate;
import org.vaadin.virkki.cdiutils.application.UIContext.UIScoped;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TestView extends VerticalLayout {

    @Inject
    @Preconfigured(caption = "test")
    private Button testButton;
    @Inject
    @Preconfigured(caption = "dereference")
    private Button dereferenceButton;
    @Inject
    private Instance<TestBean> testBean;
    @Inject
    private ContextDelegate contextDelegate;

    @PostConstruct
    public void postConstruct() {
        addComponent(testButton);
        testButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                Notification.show("Bean date: "
                        + testBean.get().getCurrentDate());
            }
        });
        addComponent(dereferenceButton);
        dereferenceButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                contextDelegate.dereferenceBeanInstance(TestBean.class);
            }
        });
    }

    @UIScoped
    public static class TestBean {
        private final Date currentDate = new Date();

        public Date getCurrentDate() {
            return currentDate;
        }

    }
}
