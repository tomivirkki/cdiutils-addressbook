package org.vaadin.virkki.cdiutils.addressbook.ui.search;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.addon.cdiproperties.annotation.ButtonProperties;
import org.vaadin.addon.cdiproperties.annotation.CheckBoxProperties;
import org.vaadin.addon.cdiproperties.annotation.NativeSelectProperties;
import org.vaadin.addon.cdiproperties.annotation.PanelProperties;
import org.vaadin.addon.cdiproperties.annotation.TextFieldProperties;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.PersonList;
import org.vaadin.virkki.cdiutils.addressbook.ui.main.MainPresenter;

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
public class SearchViewImpl extends AbstractMVPView implements SearchView {

    @Inject
    @TextFieldProperties(captionKey = "searchview-searchterm", nullRepresentation = "")
    private TextField searchTerm;
    @Inject
    @NativeSelectProperties(captionKey = "searchview-fieldtosearch")
    private NativeSelect fieldToSearch;
    @Inject
    @CheckBoxProperties(captionKey = "searchview-savesearch", immediate = true)
    private CheckBox saveSearch;
    @Inject
    @TextFieldProperties(captionKey = "searchview-searchname", nullRepresentation = "")
    private TextField searchName;
    @Inject
    @PanelProperties(captionKey = "searchview-caption")
    private Panel mainPanel;
    @Inject
    @ButtonProperties(captionKey = "searchview-search")
    private Button searchButton;
    @Inject
    private TextBundle tb;

    private SearchFilter searchFilter;

    @PostConstruct
    protected void initView() {
        final FormLayout mainLayout = new FormLayout();
        mainPanel.setContent(mainLayout);
        mainPanel.setContent(mainLayout);
        setCompositionRoot(mainPanel);

        addStyleName("view");
        setSizeFull();

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

        mainLayout.addComponent(searchName);

        searchButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                performSearch();
            }
        });
        mainLayout.addComponent(searchButton);

        localize(null);

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
            final String errorText = tb.getText("searchview-error-termempty");
            Notification.show(errorText, Notification.Type.WARNING_MESSAGE);

        } else if (saveSearch.getValue()) {
            if (searchFilter.getSearchName() == null
                    || searchFilter.getSearchName().isEmpty()) {
                final String errorText = tb
                        .getText("searchview-error-filternameempty");
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

    protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameters) {
        for (int i = 0; i < PersonList.NATURAL_COL_ORDER.length; i++) {
            final String header = tb.getText("person-"
                    + String.valueOf(PersonList.NATURAL_COL_ORDER[i])
                            .toLowerCase());
            fieldToSearch.setItemCaption(PersonList.NATURAL_COL_ORDER[i],
                    header);
        }
    }
}
