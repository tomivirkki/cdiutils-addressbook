package org.vaadin.virkki.cdiutils.addressbook.ui.search;

import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.PersonList;
import org.vaadin.virkki.cdiutils.addressbook.ui.main.MainPresenter;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.AbstractView;

import com.vaadin.cdi.UIScoped;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
@UIScoped
public class SearchViewImpl extends AbstractView implements SearchView {

    @Inject
    @Preconfigured(captionKey = "searchview-searchterm")
    private TextField searchTerm;
    @Inject
    @Preconfigured(captionKey = "searchview-fieldtosearch")
    private NativeSelect fieldToSearch;
    @Inject
    @Preconfigured(captionKey = "searchview-savesearch", immediate = true)
    private CheckBox saveSearch;
    @Inject
    @Preconfigured(captionKey = "searchview-searchname")
    private TextField searchName;
    @Inject
    @Preconfigured(captionKey = "searchview-caption")
    private Panel mainPanel;
    @Inject
    @Preconfigured(captionKey = "searchview-search")
    private Button searchButton;

    private SearchFilter searchFilter;

    @Override
    protected void initView() {
        final FormLayout mainLayout = new FormLayout();
        mainPanel.setContent(mainLayout);
        mainPanel.setContent(mainLayout);
        setCompositionRoot(mainPanel);

        addStyleName("view");
        setSizeFull();

        searchTerm.setNullRepresentation("");
        mainLayout.addComponent(searchTerm);

        constructFieldToSearch();
        mainLayout.addComponent(fieldToSearch);

        saveSearch.setValue(true);
        saveSearch.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final ValueChangeEvent event) {
                searchName.setVisible(saveSearch.getValue());
            }
        });
        mainLayout.addComponent(saveSearch);

        searchName.setNullRepresentation("");
        mainLayout.addComponent(searchName);

        searchButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                performSearch();
            }
        });
        mainLayout.addComponent(searchButton);

        localize();

    }

    private void constructFieldToSearch() {
        for (int i = 0; i < PersonList.NATURAL_COL_ORDER.length; i++) {
            fieldToSearch.addItem(PersonList.NATURAL_COL_ORDER[i]);
        }
        fieldToSearch.setNullSelectionAllowed(false);
    }

    private void performSearch() {
        final String searchTerm = searchFilter.getTerm();
        if ((searchTerm == null) || searchTerm.equals("")) {
            final String errorText = getText("searchview-error-termempty");
            Notification.show(errorText, Notification.Type.WARNING_MESSAGE);

        } else if (saveSearch.booleanValue()) {
            if (searchFilter.getSearchName() == null
                    || searchFilter.getSearchName().isEmpty()) {
                final String errorText = getText("searchview-error-filternameempty");
                Notification.show(errorText, Notification.Type.WARNING_MESSAGE);
            } else {
                fireViewEvent(MainPresenter.SAVE_SEARCH, searchFilter);
            }

        } else {
            fireViewEvent(MainPresenter.SEARCH, searchFilter);
        }

    }

    @Override
    public void editNewSearchFilter(final SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
        searchTerm.setPropertyDataSource(new MethodProperty<String>(
                searchFilter, SearchFilter.Fields.term.name()));
        fieldToSearch.setPropertyDataSource(new MethodProperty<String>(
                searchFilter, SearchFilter.Fields.propertyId.name()));
        searchName.setPropertyDataSource(new MethodProperty<String>(
                searchFilter, SearchFilter.Fields.searchName.name()));
        saveSearch.setValue(true);
    }

    @Override
    protected void localize() {
        for (int i = 0; i < PersonList.NATURAL_COL_ORDER.length; i++) {
            final String header = getText("person-"
                    + String.valueOf(PersonList.NATURAL_COL_ORDER[i])
                            .toLowerCase());
            fieldToSearch.setItemCaption(PersonList.NATURAL_COL_ORDER[i],
                    header);
        }
    }
}
