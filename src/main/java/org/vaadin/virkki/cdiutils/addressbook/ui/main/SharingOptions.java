package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.application.UIContext.UIScoped;

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

    public void init() {
        final VerticalLayout mainLayout = new VerticalLayout();
        setContent(mainLayout);

        setModal(true);
        setWidth(50.0f, Unit.PERCENTAGE);
        center();
        setCaption(lang.getText("sharingoptions-caption"));
        mainLayout.addComponent(new Label(lang
                .getText("sharingoptions-content")));
        mainLayout.addComponent(new CheckBox(lang
                .getText("sharingoptions-gmail")));
        mainLayout
                .addComponent(new CheckBox(lang.getText("sharingoptions-mac")));
        final Button close = new Button(lang.getText("ok"));
        close.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                SharingOptions.this.close();
            }
        });
        mainLayout.addComponent(close);
    }
}
