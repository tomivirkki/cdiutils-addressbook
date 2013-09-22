package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.MVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.addon.cdiproperties.annotation.TreeProperties;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.ListView;
import org.vaadin.virkki.cdiutils.addressbook.ui.search.SearchView;

import com.vaadin.cdi.UIScoped;
import com.vaadin.data.Property;
import com.vaadin.ui.Tree;

@SuppressWarnings("serial")
@UIScoped
public class NavigationTree extends ViewComponent {
    @Inject
    @TreeProperties(nullSelectionAllowed = false, immediate = true, selectable = true)
    private Tree tree;
    @Inject
    private TextBundle tb;

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

    @PostConstruct
    public void init() {

        tree.addItem(MainPresenter.SHOW_ALL);
        tree.setChildrenAllowed(MainPresenter.SHOW_ALL, false);

        tree.addItem(MainPresenter.NEW_SEARCH);

        tree.addValueChangeListener(listener);
        setCompositionRoot(tree);

        localize(null);
    }

    public void setSelectedView(final Class<? extends MVPView> viewClass) {
        tree.removeValueChangeListener(listener);
        if (SearchView.class.isAssignableFrom(viewClass)) {
            setValue(MainPresenter.NEW_SEARCH);
        } else if (ListView.class.isAssignableFrom(viewClass)) {
            setValue(MainPresenter.SHOW_ALL);
        }
        tree.addValueChangeListener(listener);
    }

    public void addSearchToTree(final SearchFilter searchFilter) {
        tree.addItem(searchFilter);
        tree.setParent(searchFilter, MainPresenter.NEW_SEARCH);
        tree.setChildrenAllowed(searchFilter, false);
        tree.expandItem(MainPresenter.NEW_SEARCH);
        tree.setValue(searchFilter);
    }

    public void setValue(final String value) {
        tree.setValue(value);
    }

    protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
        tree.setItemCaption(MainPresenter.SHOW_ALL,
                tb.getText("navigation-showall"));
        tree.setItemCaption(MainPresenter.NEW_SEARCH,
                tb.getText("navigation-search"));
    }
}
