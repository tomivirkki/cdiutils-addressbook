package org.vaadin.virkki.cdiutils.addressbook.ui.test;

import javax.annotation.PreDestroy;

import org.vaadin.virkki.cdiutils.mvp.AbstractPresenter;
import org.vaadin.virkki.cdiutils.mvp.AbstractPresenter.ViewInterface;

@SuppressWarnings("serial")
@ViewInterface(TestView.class)
public class TestPresenter extends AbstractPresenter<TestView> {

    @Override
    protected void initPresenter() {
        // NOP
    }

    @Override
    public final void viewOpened() {
        // NOP
    }

    @PreDestroy
    public final void preDestroy() {
        logger.info("TestPresenter instance destroyed");
    }
}
