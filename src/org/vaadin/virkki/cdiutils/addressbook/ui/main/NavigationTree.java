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
	public static final Object SHOW_ALL = "showall";
	public static final Object SEARCH = "search";

	@Inject
	private javax.enterprise.event.Event<ParameterDTO> viewEvent;
	@Inject
	private Lang lang;

	private final Property.ValueChangeListener listener = new Property.ValueChangeListener() {
		@Override
		public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			Object itemId = event.getProperty().getValue();
			if (itemId != null) {
				if (NavigationTree.SHOW_ALL.equals(itemId)) {
					fireViewEvent(MainPresenter.SHOW_ALL, null);
				} else if (NavigationTree.SEARCH.equals(itemId)) {
					fireViewEvent(MainPresenter.NEW_SEARCH, null);
				} else if (itemId instanceof SearchFilter) {
					fireViewEvent(MainPresenter.SEARCH, itemId);
				}
			}
		}
	};

	public void init() {
		addItem(SHOW_ALL);
		setItemCaption(SHOW_ALL, lang.getText("navigation-showall"));
		addItem(SEARCH);
		setItemCaption(SEARCH, lang.getText("navigation-search"));

		setChildrenAllowed(SHOW_ALL, false);
		setSelectable(true);
		setNullSelectionAllowed(false);
		setImmediate(true);

		addListener(listener);
	}

	protected void fireViewEvent(String methodIdentifier, Object primaryParameter, Object... secondaryParameters) {
		viewEvent.select(new EventQualifierImpl(methodIdentifier, View.class) {}).fire(new ParameterDTO(primaryParameter, secondaryParameters));
	}

	public void setSelectedView(Class<? extends View> viewClass) {
		removeListener(listener);
		if (SearchView.class.isAssignableFrom(viewClass)) {
			setValue(SEARCH);
		} else if (ListView.class.isAssignableFrom(viewClass)) {
			setValue(SHOW_ALL);
		}
		addListener(listener);
	}

	public void addSearchToTree(SearchFilter searchFilter) {
		addItem(searchFilter);
		setParent(searchFilter, NavigationTree.SEARCH);
		setChildrenAllowed(searchFilter, false);
		expandItem(NavigationTree.SEARCH);
		setValue(searchFilter);
	}
}
