package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addon.cdiproperties.annotation.ButtonProperties;
import org.vaadin.addon.cdiproperties.annotation.HorizontalLayoutProperties;
import org.vaadin.virkki.cdiutils.addressbook.util.Lang;

import com.vaadin.cdi.UIScoped;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@UIScoped
public class Toolbar extends ViewComponent {
    @Inject
    @HorizontalLayoutProperties(styleName = "toolbar", spacing = true, widthValue = 100.0f, widthUnits = Unit.PERCENTAGE)
    private HorizontalLayout layout;
    @Inject
    @ButtonProperties(captionKey = "toolbar-addcontact")
    private Button addContactButton;
    @Inject
    @ButtonProperties(captionKey = "toolbar-search")
    private Button searchButton;
    @Inject
    @ButtonProperties(captionKey = "toolbar-share")
    private Button shareButton;
    @Inject
    @ButtonProperties(captionKey = "toolbar-help")
    private Button helpButton;

    @PostConstruct
    public void init() {
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
                        UI.getCurrent().setLocale(Lang.EN_US);
                    }
                });
        layout.addComponent(enButton);

        final NativeButton fiButton = new NativeButton("FI",
                new ClickListener() {
                    @Override
                    public void buttonClick(final ClickEvent event) {
                        UI.getCurrent().setLocale(Lang.FI_FI);
                    }
                });
        layout.addComponent(fiButton);

        setCompositionRoot(layout);
    }

    private void addToolbarButton(final Button button, final String iconUrl,
            final String methodId) {
        button.setIcon(new ThemeResource("../runo/" + iconUrl));
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                fireViewEvent(methodId, null);
            }
        });
        layout.addComponent(button);
    }
}
