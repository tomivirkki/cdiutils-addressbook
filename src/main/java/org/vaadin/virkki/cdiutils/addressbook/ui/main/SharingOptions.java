package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.componentproducers.Localizer;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.CDIEvent;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;

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
    @Preconfigured(labelValueKey = "sharingoptions-content")
    private Label contentLabel;
    @Inject
    @Preconfigured(captionKey = "sharingoptions-gmail")
    private CheckBox gmailCheckBox;
    @Inject
    @Preconfigured(captionKey = "sharingoptions-mac")
    private CheckBox macCheckBox;
    @Inject
    @Preconfigured(captionKey = "ok")
    private Button okButton;

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
            @Observes(notifyObserver = Reception.IF_EXISTS) @CDIEvent(Localizer.UPDATE_LOCALIZED_VALUES) final ParameterDTO parameters) {
        setCaption(lang.getText("sharingoptions-caption"));
    }

}
