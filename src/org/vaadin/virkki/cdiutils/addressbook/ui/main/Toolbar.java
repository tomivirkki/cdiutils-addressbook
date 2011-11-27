package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.mvp.AbstractView.EventQualifierImpl;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;
import org.vaadin.virkki.cdiutils.mvp.View;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
@SessionScoped
public class Toolbar extends HorizontalLayout {
	@Inject
	private javax.enterprise.event.Event<ParameterDTO> navigationEvent;
	@Inject
	private Lang lang;

	public void init() {
		setStyleName("toolbar");
		setMargin(true);
		setSpacing(true);
		setWidth(100.0f, UNITS_PERCENTAGE);

		addToolbarButton("toolbar-addcontact", "icons/32/document-add.png", MainPresenter.NEW_CONTACT);
		addToolbarButton("toolbar-search", "icons/32/folder-add.png", MainPresenter.NEW_SEARCH);
		addToolbarButton("toolbar-share", "icons/32/users.png", MainPresenter.SHARE);
		addToolbarButton("toolbar-help", "icons/32/help.png", MainPresenter.HELP);

		Embedded em = new Embedded("", new ThemeResource("images/logo.png"));
		addComponent(em);
		setComponentAlignment(em, Alignment.MIDDLE_RIGHT);
		setExpandRatio(em, 1);
	}

	private void addToolbarButton(String captionKey, String iconUrl, final String methodId) {
		Button button = new Button(lang.getText(captionKey));
		button.setIcon(new ThemeResource(iconUrl));
		button.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				navigationEvent.select(new EventQualifierImpl(methodId, View.class) {}).fire(new ParameterDTO(null));
			}
		});
		addComponent(button);
	}
}
