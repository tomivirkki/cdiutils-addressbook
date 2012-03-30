package org.vaadin.virkki.cdiutils.addressbook.ui.test;

import java.util.Date;

import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped;
import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped.VaadinScope;

public class TestBeans {

    @VaadinScoped
    public static class WindowBean {
        private final Date date = new Date();

        public final String sayWhen() {
            return date.toString();
        }

    }

    @VaadinScoped(VaadinScope.APPLICATION)
    public static class ApplicationBean {
        private final Date date = new Date();

        public final String sayWhen() {
            return date.toString();
        }
    }
}