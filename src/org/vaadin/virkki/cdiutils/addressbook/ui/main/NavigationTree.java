package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.ListView;
import org.vaadin.virkki.cdiutils.addressbook.ui.search.SearchView;
import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped;
import org.vaadin.virkki.cdiutils.componentproducers.Localizer;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.CDIEvent;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;
import org.vaadin.virkki.cdiutils.mvp.View;
import org.vaadin.virkki.cdiutils.mvp.ViewComponent;

import com.vaadin.data.Property;
import com.vaadin.ui.Tree;

@SuppressWarnings("serial")
@VaadinScoped
public class NavigationTree extends ViewComponent {
    @Inject
    @Preconfigured(nullSelectionAllowed = false, immediate = true)
    private Tree tree;

    private final Property.ValueChangeListener listener = new Property.ValueChangeListener() {
        @Override
        public void valueChange(
                final com.vaadin.data.Property.ValueChangeEvent event) {
            final Object itemId = event.getProperty().getValue();
            if (itemId != null) {
                if (itemId instanceof SearchFilter) {
                    fireViewEvent(MainPresenter.SEARCH, itemId);
                } else {
                    fireViewEvent((String) itemId, null);
                }
            }
        }
    };

    public final void init() {
        tree.setSelectable(true);

        tree.addItem(MainPresenter.SHOW_ALL);
        tree.setChildrenAllowed(MainPresenter.SHOW_ALL, false);

        tree.addItem(MainPresenter.NEW_SEARCH);

        tree.addListener(listener);
        setCompositionRoot(tree);

        localize(null);
    }

    public final void setSelectedView(final Class<? extends View> viewClass) {
        tree.removeListener(listener);
        if (SearchView.class.isAssignableFrom(viewClass)) {
            setValue(MainPresenter.NEW_SEARCH);
        } else if (ListView.class.isAssignableFrom(viewClass)) {
            setValue(MainPresenter.SHOW_ALL);
        }
        tree.addListener(listener);
    }

    public final void addSearchToTree(final SearchFilter searchFilter) {
        tree.addItem(searchFilter);
        tree.setParent(searchFilter, MainPresenter.NEW_SEARCH);
        tree.setChildrenAllowed(searchFilter, false);
        tree.expandItem(MainPresenter.NEW_SEARCH);
        tree.setValue(searchFilter);
    }

    public final void setValue(final String value) {
        tree.setValue(value);
    }

    protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @CDIEvent(Localizer.UPDATE_LOCALIZED_VALUES) final ParameterDTO parameterDto) {
        tree.setItemCaption(MainPresenter.SHOW_ALL,
                getText("navigation-showall"));
        tree.setItemCaption(MainPresenter.NEW_SEARCH,
                getText("navigation-search"));
    }
}
