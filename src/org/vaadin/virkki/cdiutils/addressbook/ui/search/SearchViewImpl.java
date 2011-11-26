package org.vaadin.virkki.cdiutils.addressbook.ui.search;

import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.Person;
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
	private javax.enterprise.event.Event<ParameterDTO> viewEvent;

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
		FormLayout formLayout = new FormLayout();
		mainPanel.setContent(formLayout);
		setCompositionRoot(mainPanel);

		addStyleName("view");
		setCaption(getText("searchview-caption"));
		setSizeFull();

		searchTerm.setNullRepresentation("");
		mainPanel.addComponent(searchTerm);

		for (int i = 0; i < PersonList.NATURAL_COL_ORDER.length; i++) {
			fieldToSearch.addItem(PersonList.NATURAL_COL_ORDER[i]);

			String header = getText("person-"
					+ String.valueOf(PersonList.NATURAL_COL_ORDER[i])
							.toLowerCase());
			fieldToSearch.setItemCaption(PersonList.NATURAL_COL_ORDER[i],
					header);
		}
		fieldToSearch.setNullSelectionAllowed(false);
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

		Button search = new Button("Search", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				performSearch();
			}
		});
		mainPanel.addComponent(search);
	}

	private void performSearch() {
		String searchTerm = searchFilter.getTerm();
		if ((searchTerm == null) || searchTerm.equals("")) {
			getWindow().showNotification("Search term cannot be empty!",
					Notification.TYPE_WARNING_MESSAGE);
			return;
		}

		if (saveSearch.booleanValue()) {
			if (searchFilter.getSearchName() == null
					|| searchFilter.getSearchName().isEmpty()) {
				getWindow().showNotification(
						"Please enter a name for your search!",
						Notification.TYPE_WARNING_MESSAGE);
				return;
			}
			fireViewEvent(MainPresenter.SAVE_SEARCH, searchFilter,
					saveSearch.booleanValue());
		} else {
			viewEvent.select(
					new EventQualifierImpl(MainPresenter.SEARCH, View.class) {
					}).fire(new ParameterDTO(searchFilter));
		}

	}

	@Override
	public void editNewSearchFilter(SearchFilter searchFilter) {
		this.searchFilter = searchFilter;
		searchTerm.setPropertyDataSource(new MethodProperty<String>(
				searchFilter, SearchFilter.Fields.term.name()));
		fieldToSearch.setPropertyDataSource(new MethodProperty<String>(
				searchFilter, SearchFilter.Fields.propertyId.name()));
		searchName.setPropertyDataSource(new MethodProperty<String>(
				searchFilter, SearchFilter.Fields.searchName.name()));
		saveSearch.setValue(true);
		fieldToSearch.setValue(Person.Fields.lastName.name());
	}

}
