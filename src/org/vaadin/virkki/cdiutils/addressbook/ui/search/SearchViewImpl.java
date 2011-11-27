package org.vaadin.virkki.cdiutils.addressbook.ui.search;

import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.PersonList;
import org.vaadin.virkki.cdiutils.addressbook.ui.main.MainPresenter;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.AbstractView;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;
import org.vaadin.virkki.cdiutils.mvp.View;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window.Notification;

@SuppressWarnings("serial")
public class SearchViewImpl extends AbstractView implements SearchView {
	@Inject
	private javax.enterprise.event.Event<ParameterDTO> searchEvent;

	@Inject
	@Preconfigured(captionKey = "searchview-searchterm")
	private TextField searchTerm;
	@Inject
	@Preconfigured(captionKey = "searchview-fieldtosearch")
	private NativeSelect fieldToSearch;
	@Inject
	@Preconfigured(captionKey = "searchview-savesearch", immediate = true, implementation = CheckBox.class)
	private Button saveSearch;
	@Inject
	@Preconfigured(captionKey = "searchview-searchname")
	private TextField searchName;

	private SearchFilter searchFilter;

	@Override
	protected void initView() {
		Panel mainPanel = new Panel();
		mainPanel.setCaption(getText("searchview-caption"));
		mainPanel.setContent(new FormLayout());
		setCompositionRoot(mainPanel);

		addStyleName("view");
		setSizeFull();

		searchTerm.setNullRepresentation("");
		mainPanel.addComponent(searchTerm);

		constructFieldToSearch();
		mainPanel.addComponent(fieldToSearch);

		saveSearch.setValue(true);
		saveSearch.addListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				searchName.setVisible(saveSearch.booleanValue());
			}
		});
		mainPanel.addComponent(saveSearch);

		searchName.setNullRepresentation("");
		mainPanel.addComponent(searchName);

		Button search = new Button(getText("searchview-search"), new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				performSearch();
			}
		});
		mainPanel.addComponent(search);
	}

	private void constructFieldToSearch() {
		for (int i = 0; i < PersonList.NATURAL_COL_ORDER.length; i++) {
			fieldToSearch.addItem(PersonList.NATURAL_COL_ORDER[i]);

			String header = getText("person-" + String.valueOf(PersonList.NATURAL_COL_ORDER[i]).toLowerCase());
			fieldToSearch.setItemCaption(PersonList.NATURAL_COL_ORDER[i], header);
		}
		fieldToSearch.setNullSelectionAllowed(false);
	}

	private void performSearch() {
		String searchTerm = searchFilter.getTerm();
		if ((searchTerm == null) || searchTerm.equals("")) {
			String errorText = getText("searchview-error-termempty");
			getWindow().showNotification(errorText, Notification.TYPE_WARNING_MESSAGE);

		} else if (saveSearch.booleanValue()) {
			if (searchFilter.getSearchName() == null || searchFilter.getSearchName().isEmpty()) {
				String errorText = getText("searchview-error-filternameempty");
				getWindow().showNotification(errorText, Notification.TYPE_WARNING_MESSAGE);
			} else {
				fireSearchEvent(MainPresenter.SAVE_SEARCH, searchFilter);
			}

		} else {
			fireSearchEvent(MainPresenter.SEARCH, searchFilter);
		}

	}

	@Override
	public void editNewSearchFilter(SearchFilter searchFilter) {
		this.searchFilter = searchFilter;
		searchTerm.setPropertyDataSource(new MethodProperty<String>(searchFilter, SearchFilter.Fields.term.name()));
		fieldToSearch.setPropertyDataSource(new MethodProperty<String>(searchFilter, SearchFilter.Fields.propertyId.name()));
		searchName.setPropertyDataSource(new MethodProperty<String>(searchFilter, SearchFilter.Fields.searchName.name()));
		saveSearch.setValue(true);
	}

	protected void fireSearchEvent(String methodIdentifier, SearchFilter searchFilter) {
		searchEvent.select(new EventQualifierImpl(methodIdentifier, View.class) {}).fire(new ParameterDTO(searchFilter));
	}
}
