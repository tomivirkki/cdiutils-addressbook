package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.annotation.ButtonProperties;
import org.vaadin.addon.cdiproperties.annotation.CheckBoxProperties;
import org.vaadin.addon.cdiproperties.annotation.LabelProperties;
import org.vaadin.virkki.cdiutils.addressbook.util.Lang;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@UIScoped
@SuppressWarnings("serial")
public class SharingOptions extends Window {
    @Inject
    private Lang lang;
    @Inject
    @LabelProperties(valueKey = "sharingoptions-content")
    private Label contentLabel;
    @Inject
    @CheckBoxProperties(captionKey = "sharingoptions-gmail")
    private CheckBox gmailCheckBox;
    @Inject
    @CheckBoxProperties(captionKey = "sharingoptions-mac")
    private CheckBox macCheckBox;
    @Inject
    @ButtonProperties(captionKey = "ok")
    private Button okButton;

    @PostConstruct
    public void init() {
        final VerticalLayout mainLayout = new VerticalLayout();
        setContent(mainLayout);

        setModal(true);
        setWidth(50.0f, Unit.PERCENTAGE);
        center();

        mainLayout.addComponent(contentLabel);
        mainLayout.addComponent(gmailCheckBox);
        mainLayout.addComponent(macCheckBox);
        okButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                SharingOptions.this.close();
            }
        });
        mainLayout.addComponent(okButton);

        localize(null);
    }

    void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameters) {
        setCaption(lang.getText("sharingoptions-caption"));
    }

}
