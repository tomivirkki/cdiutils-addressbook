package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.ListView;
import org.vaadin.virkki.cdiutils.addressbook.ui.search.SearchView;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.View;
import org.vaadin.virkki.cdiutils.mvp.ViewComponent;

import com.vaadin.data.Property;
import com.vaadin.ui.Tree;

@SuppressWarnings("serial")
@SessionScoped
public class NavigationTree extends ViewComponent {
	@Inject
	@Preconfigured(nullSelectionAllowed = false, immediate = true)
	private Tree tree;

	private final Property.ValueChangeListener listener = new Property.ValueChangeListener() {
		@Override
		public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			Object itemId = event.getProperty().getValue();
			if (itemId != null) {
				if (itemId instanceof SearchFilter) {
					fireViewEvent(MainPresenter.SEARCH, View.class, itemId);
				} else {
					fireViewEvent((String) itemId, View.class, null);
				}
			}
		}
	};

	public void init() {
		tree.setSelectable(true);

		tree.addItem(MainPresenter.SHOW_ALL);
		tree.setItemCaption(MainPresenter.SHOW_ALL, getText("navigation-showall"));
		tree.setChildrenAllowed(MainPresenter.SHOW_ALL, false);

		tree.addItem(MainPresenter.NEW_SEARCH);
		tree.setItemCaption(MainPresenter.NEW_SEARCH, getText("navigation-search"));

		tree.addListener(listener);
		setCompositionRoot(tree);
	}

	public void setSelectedView(Class<? extends View> viewClass) {
		tree.removeListener(listener);
		if (SearchView.class.isAssignableFrom(viewClass)) {
			setValue(MainPresenter.NEW_SEARCH);
		} else if (ListView.class.isAssignableFrom(viewClass)) {
			setValue(MainPresenter.SHOW_ALL);
		}
		tree.addListener(listener);
	}

	public void addSearchToTree(SearchFilter searchFilter) {
		tree.addItem(searchFilter);
		tree.setParent(searchFilter, MainPresenter.NEW_SEARCH);
		tree.setChildrenAllowed(searchFilter, false);
		tree.expandItem(MainPresenter.NEW_SEARCH);
		tree.setValue(searchFilter);
	}

	public void setValue(String value) {
		tree.setValue(value);
	}
}
