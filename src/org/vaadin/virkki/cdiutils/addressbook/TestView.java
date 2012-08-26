package org.vaadin.virkki.cdiutils.addressbook;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.application.ContextHandle;
import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@VaadinScoped
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
    private ContextHandle contextHandle;

    @PostConstruct
    public void postConstruct() {
        addComponent(testButton);
        testButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                Notification.show("Bean date: "
                        + testBean.get().getCurrentDate());
            }
        });
        addComponent(dereferenceButton);
        dereferenceButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                contextHandle.dereferenceBeanInstance(TestBean.class);
            }
        });
    }

    @VaadinScoped
    public static class TestBean {
        private final Date currentDate = new Date();

        public Date getCurrentDate() {
            return currentDate;
        }

    }
}
