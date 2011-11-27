package org.vaadin.virkki.cdiutils.addressbook.ui.list;

import java.util.Collection;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.AddressBookApplication;
import org.vaadin.virkki.cdiutils.addressbook.data.Person;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.mvp.AbstractView.EventQualifierImpl;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window.Notification;

@SuppressWarnings("serial")
@SessionScoped
public class PersonList extends Table {
	@Inject
	private javax.enterprise.event.Event<ParameterDTO> viewEvent;

	@Inject
	private Lang lang;

	/*
	 * The Application instance is needed in this class so it's simply injected
	 * here as a field. Applications are session scoped so we can be sure to
	 * receive the right instance.
	 */
	@Inject
	private Instance<AddressBookApplication> app;

	public static final Object[] NATURAL_COL_ORDER = new Object[] { Person.Fields.firstName.name(), Person.Fields.lastName.name(), Person.Fields.email.name(),
			Person.Fields.phoneNumber.name(), Person.Fields.streetAddress.name(), Person.Fields.postalCode.name(), Person.Fields.city.name() };

	public void init() {
		setSizeFull();
		setColumnCollapsingAllowed(true);
		setColumnReorderingAllowed(true);
		setSelectable(true);
		setImmediate(true);
		setNullSelectionAllowed(false);

		addListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				Person person = (Person) event.getProperty().getValue();
				if (person != null) {
					viewEvent.select(new EventQualifierImpl(ListPresenter.PERSON_SELECTED, ListView.class) {}).fire(new ParameterDTO(person));
				}
			}
		});

		initColumns();
	}

	private void initColumns() {
		setContainerDataSource(new BeanItemContainer<Person>(Person.class));
		setVisibleColumns(NATURAL_COL_ORDER);
		for (Object propertyId : getVisibleColumns()) {
			String header = lang.getText("person-" + String.valueOf(propertyId).toLowerCase());
			setColumnHeader(propertyId, header);
		}

		addGeneratedColumn(Person.Fields.email.name(), new ColumnGenerator() {
			@Override
			public Component generateCell(Table source, Object itemId, Object columnId) {
				String email = ((Person) itemId).getEmail();
				return new Link(email, new ExternalResource("mailto:" + email));
			}
		});

		setSortContainerPropertyId(Person.Fields.firstName.name());
	}

	public void setPersonList(Collection<Person> people) {
		removeAllItems();
		for (Person person : people) {
			addItem(person);
		}
		sort();
	}

	public void addContactToList(Person person) {
		addItem(person);
		sort();
	}

	@SuppressWarnings("unchecked")
	public void applyFilter(SearchFilter searchFilter) {
		BeanItemContainer<Person> container = (BeanItemContainer<Person>) getContainerDataSource();
		// clear previous filters
		container.removeAllContainerFilters();

		if (searchFilter != null) {
			// filter contacts with given filter
			container.addContainerFilter(searchFilter.getPropertyId(), searchFilter.getTerm(), true, false);

			String propertyName = lang.getText("person-" + String.valueOf(searchFilter.getPropertyId()).toLowerCase());
			/*
			 * personlist-searchnotification -text has 3 parameters which are
			 * passed in the Lang.getText-method.
			 */
			String notificationText = lang.getText("personlist-searchnotification", propertyName, searchFilter.getTerm(), container.size());
			app.get().getMainWindow().showNotification(notificationText, Notification.TYPE_TRAY_NOTIFICATION);
		}
	}

}