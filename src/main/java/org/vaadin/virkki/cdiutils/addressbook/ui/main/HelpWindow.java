package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.annotation.LabelProperties;
import org.vaadin.virkki.cdiutils.addressbook.util.Lang;

import com.vaadin.cdi.UIScoped;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@UIScoped
@SuppressWarnings("serial")
public class HelpWindow extends Window {
    @Inject
    private Lang lang;
    @Inject
    @LabelProperties(valueKey = "helpwindow-content", contentMode = ContentMode.HTML)
    private Label label;

    @PostConstruct
    public void init() {
        final VerticalLayout mainLayout = new VerticalLayout(label);
        setContent(mainLayout);
        localize(null);
    }

    void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameters) {
        setCaption(lang.getText("helpwindow-caption"));
    }

}
