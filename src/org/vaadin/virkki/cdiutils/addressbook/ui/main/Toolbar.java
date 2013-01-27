package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.ViewComponent;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;

@SuppressWarnings("serial")
@VaadinScoped
public class Toolbar extends ViewComponent {
    @Inject
    @Preconfigured(styleName = "toolbar", margin = true, spacing = true, width = 100.0f, widthUnits = UNITS_PERCENTAGE)
    private HorizontalLayout layout;
    @Inject
    @Preconfigured(captionKey = "toolbar-addcontact")
    private Button addContactButton;
    @Inject
    @Preconfigured(captionKey = "toolbar-search")
    private Button searchButton;
    @Inject
    @Preconfigured(captionKey = "toolbar-share")
    private Button shareButton;
    @Inject
    @Preconfigured(captionKey = "toolbar-help")
    private Button helpButton;

    public final void init() {
        addToolbarButton(addContactButton, "icons/32/document-add.png",
                MainPresenter.NEW_CONTACT);
        addToolbarButton(searchButton, "icons/32/folder-add.png",
                MainPresenter.NEW_SEARCH);
        addToolbarButton(shareButton, "icons/32/users.png", MainPresenter.SHARE);
        addToolbarButton(helpButton, "icons/32/help.png", MainPresenter.HELP);

        final Embedded em = new Embedded("", new ThemeResource(
                "images/logo.png"));
        layout.addComponent(em);
        layout.setComponentAlignment(em, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(em, 1);

        final NativeButton enButton = new NativeButton("EN",
                new ClickListener() {
                    @Override
                    public void buttonClick(final ClickEvent event) {
                        getContextApplication().setLocale(Lang.EN_US);
                    }
                });
        layout.addComponent(enButton);

        final NativeButton fiButton = new NativeButton("FI",
                new ClickListener() {
                    @Override
                    public void buttonClick(final ClickEvent event) {
                        getContextApplication().setLocale(Lang.FI_FI);
                    }
                });
        layout.addComponent(fiButton);

        setCompositionRoot(layout);
    }

    private void addToolbarButton(final Button button, final String iconUrl,
            final String methodId) {
        button.setIcon(new ThemeResource("../runo/" + iconUrl));
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                fireViewEvent(methodId, null);
            }
        });
        layout.addComponent(button);
    }
}
