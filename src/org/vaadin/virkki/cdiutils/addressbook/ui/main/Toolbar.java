package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.View;
import org.vaadin.virkki.cdiutils.mvp.ViewComponent;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
@SessionScoped
public class Toolbar extends ViewComponent {
	@Inject
	@Preconfigured(styleName = "toolbar", margin = true, spacing = true, width = 100.0f, widthUnits = UNITS_PERCENTAGE)
	private HorizontalLayout layout;

	public void init() {
		addToolbarButton("toolbar-addcontact", "icons/32/document-add.png", MainPresenter.NEW_CONTACT);
		addToolbarButton("toolbar-search", "icons/32/folder-add.png", MainPresenter.NEW_SEARCH);
		addToolbarButton("toolbar-share", "icons/32/users.png", MainPresenter.SHARE);
		addToolbarButton("toolbar-help", "icons/32/help.png", MainPresenter.HELP);

		Embedded em = new Embedded("", new ThemeResource("images/logo.png"));
		layout.addComponent(em);
		layout.setComponentAlignment(em, Alignment.MIDDLE_RIGHT);
		layout.setExpandRatio(em, 1);
		setCompositionRoot(layout);
	}

	private void addToolbarButton(String captionKey, String iconUrl, final String methodId) {
		Button button = new Button(getText(captionKey));
		button.setIcon(new ThemeResource(iconUrl));
		button.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(methodId, View.class, null);
			}
		});
		layout.addComponent(button);
	}
}
