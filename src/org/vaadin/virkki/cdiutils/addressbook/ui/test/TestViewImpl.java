package org.vaadin.virkki.cdiutils.addressbook.ui.test;

import javax.annotation.PreDestroy;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.ui.test.TestBeans.ApplicationBean;
import org.vaadin.virkki.cdiutils.addressbook.ui.test.TestBeans.WindowBean;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.AbstractView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * View for CDI Utils testing.
 */
@SuppressWarnings("serial")
public class TestViewImpl extends AbstractView implements TestView {
    @Inject
    @Preconfigured()
    private VerticalLayout mainLayout;
    @Inject
    @Preconfigured(caption = "Test VaadinScope.WINDOW bean")
    private Button testWindowScoped;
    @Inject
    @Preconfigured(caption = "Dereference VaadinScope.WINDOW bean")
    private Button dereferenceWindowScoped;
    @Inject
    @Preconfigured(caption = "Test VaadinScope.APPLICATION bean")
    private Button testAppScoped;
    @Inject
    @Preconfigured(caption = "Dereference VaadinScope.APPLICATION bean")
    private Button dereferenceAppScoped;
    @Inject
    private Instance<WindowBean> windowBean;
    @Inject
    private Instance<ApplicationBean> applicationBean;

    @Override
    protected final void initView() {
        setCompositionRoot(mainLayout);

        testWindowScoped.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                getContextWindow().showNotification(windowBean.get().sayWhen());
            }
        });
        mainLayout.addComponent(testWindowScoped);
        dereferenceWindowScoped.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                getContextApplication().dereferenceBeanInstance(
                        WindowBean.class);
            }
        });
        mainLayout.addComponent(dereferenceWindowScoped);
        testAppScoped.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                getContextWindow().showNotification(
                        applicationBean.get().sayWhen());
            }
        });
        mainLayout.addComponent(testAppScoped);
        dereferenceAppScoped.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                getContextApplication().dereferenceBeanInstance(
                        ApplicationBean.class);
            }
        });
        mainLayout.addComponent(dereferenceAppScoped);
    }

    @Override
    public final void detach() {
        super.detach();
        getContextApplication().dereferenceBeanInstance(TestViewImpl.class);
    }

    @PreDestroy
    public final void preDestroy() {
        logger.info("TestViewImpl instance destroyed");
    }
}
