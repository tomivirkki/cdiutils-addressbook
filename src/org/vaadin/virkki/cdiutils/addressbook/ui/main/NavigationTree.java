package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.ListView;
import org.vaadin.virkki.cdiutils.addressbook.ui.search.SearchView;
import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.mvp.AbstractView.EventQualifierImpl;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;
import org.vaadin.virkki.cdiutils.mvp.View;

import com.vaadin.data.Property;
import com.vaadin.ui.Tree;

@SuppressWarnings("serial")
@SessionScoped
public class NavigationTree extends Tree {
	@Inject
	private javax.enterprise.event.Event<ParameterDTO> viewEvent;
	@Inject
	private Lang lang;

	private final Property.ValueChangeListener listener = new Property.ValueChangeListener() {
		@Override
		public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			Object itemId = event.getProperty().getValue();
			if (itemId != null) {
				if (itemId instanceof SearchFilter) {
					fireViewEvent(MainPresenter.SEARCH, itemId);
				} else {
					fireViewEvent((String) itemId, null);
				}
			}
		}
	};

	public void init() {
		setSelectable(true);
		setNullSelectionAllowed(false);
		setImmediate(true);

		addItem(MainPresenter.SHOW_ALL);
		setItemCaption(MainPresenter.SHOW_ALL, lang.getText("navigation-showall"));
		setChildrenAllowed(MainPresenter.SHOW_ALL, false);

		addItem(MainPresenter.NEW_SEARCH);
		setItemCaption(MainPresenter.NEW_SEARCH, lang.getText("navigation-search"));

		addListener(listener);
	}

	protected void fireViewEvent(String methodIdentifier, Object primaryParameter, Object... secondaryParameters) {
		viewEvent.select(new EventQualifierImpl(methodIdentifier, View.class) {}).fire(new ParameterDTO(primaryParameter, secondaryParameters));
	}

	public void setSelectedView(Class<? extends View> viewClass) {
		removeListener(listener);
		if (SearchView.class.isAssignableFrom(viewClass)) {
			setValue(MainPresenter.NEW_SEARCH);
		} else if (ListView.class.isAssignableFrom(viewClass)) {
			setValue(MainPresenter.SHOW_ALL);
		}
		addListener(listener);
	}

	public void addSearchToTree(SearchFilter searchFilter) {
		addItem(searchFilter);
		setParent(searchFilter, MainPresenter.NEW_SEARCH);
		setChildrenAllowed(searchFilter, false);
		expandItem(MainPresenter.NEW_SEARCH);
		setValue(searchFilter);
	}
}
