package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped;
import org.vaadin.virkki.cdiutils.componentproducers.Localizer;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.CDIEvent;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;

import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

@VaadinScoped
@SuppressWarnings("serial")
public class HelpWindow extends Window {
    @Inject
    private Lang lang;
    @Inject
    @Preconfigured(labelValueKey = "helpwindow-content")
    private Label label;

    public final void init() {
        addComponent(label);
        label.setContentMode(Label.CONTENT_XHTML);
        localize(null);
    }

    void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @CDIEvent(Localizer.UPDATE_LOCALIZED_VALUES) final ParameterDTO parameters) {
        setCaption(lang.getText("helpwindow-caption"));
    }

}
