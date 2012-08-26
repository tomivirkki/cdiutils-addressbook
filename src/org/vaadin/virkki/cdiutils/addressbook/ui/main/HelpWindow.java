package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

@VaadinScoped
@SuppressWarnings("serial")
public class HelpWindow extends Window {
    @Inject
    private Lang lang;

    public void init() {
        setCaption(lang.getText("helpwindow-caption"));
        addComponent(new Label(lang.getText("helpwindow-content"),
                ContentMode.XHTML));
    }

}
