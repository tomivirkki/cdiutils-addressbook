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
	private javax.enterprise.event.Event<ParameterDTO> viewEvent;
	@Inject
	private Lang lang;

	final Button search = new Button("Search");
	private final Button share = new Button("Share");
	private final Button help = new Button("Help");

	public void init() {
		setStyleName("toolbar");

		Button newContact = new Button(lang.getText("toolbar-addcontact"));
		newContact.setIcon(new ThemeResource("icons/32/document-add.png"));
		newContact.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MainPresenter.NEW_CONTACT, null);
			}
		});
		addComponent(newContact);

		addComponent(search);

		addComponent(share);
		share.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MainPresenter.SHARE_WINDOW_REQUESTED, null);
			}
		});

		help.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MainPresenter.HELP_WINDOW_REQUESTED, null);
			}
		});
		addComponent(help);

		search.setIcon(new ThemeResource("icons/32/folder-add.png"));
		search.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MainPresenter.NEW_SEARCH, null);
			}
		});

		share.setIcon(new ThemeResource("icons/32/users.png"));
		help.setIcon(new ThemeResource("icons/32/help.png"));

		setMargin(true);
		setSpacing(true);

		setWidth("100%");

		Embedded em = new Embedded("", new ThemeResource("images/logo.png"));
		addComponent(em);
		setComponentAlignment(em, Alignment.MIDDLE_RIGHT);
		setExpandRatio(em, 1);
	}

	protected void fireViewEvent(String methodIdentifier,
			Object primaryParameter, Object... secondaryParameters) {
		viewEvent.select(new EventQualifierImpl(methodIdentifier, View.class) {
		}).fire(new ParameterDTO(primaryParameter, secondaryParameters));
	}
}
