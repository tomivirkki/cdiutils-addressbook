package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.ViewComponent;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
@VaadinScoped
public class Toolbar extends ViewComponent {
    @Inject
    @Preconfigured(styleName = "toolbar", spacing = true, width = 100.0f, widthUnits = Unit.PERCENTAGE)
    private HorizontalLayout layout;

    public void init() {
        addToolbarButton("toolbar-addcontact", "icons/32/document-add.png",
                MainPresenter.NEW_CONTACT);
        addToolbarButton("toolbar-search", "icons/32/folder-add.png",
                MainPresenter.NEW_SEARCH);
        addToolbarButton("toolbar-share", "icons/32/users.png",
                MainPresenter.SHARE);
        addToolbarButton("toolbar-help", "icons/32/help.png",
                MainPresenter.HELP);

        final Embedded em = new Embedded("", new ThemeResource(
                "images/logo.png"));
        layout.addComponent(em);
        layout.setComponentAlignment(em, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(em, 1);
        setCompositionRoot(layout);
    }

    private void addToolbarButton(final String captionKey,
            final String iconUrl, final String methodId) {
        final Button button = new Button(getText(captionKey));
        button.setIcon(new ThemeResource(iconUrl));
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                fireViewEvent(methodId, null);
            }
        });
        layout.addComponent(button);
    }
}
