package org.vaadin.virkki.cdiutils.addressbook.ui.list;

import java.util.Collection;

import javax.enterprise.context.SessionScoped;
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
	@Inject
	private AddressBookApplication app;

	public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"firstName", "lastName", "email", "phoneNumber", "streetAddress",
			"postalCode", "city" };

	public void init() {
		setSizeFull();
		setColumnCollapsingAllowed(true);
		setColumnReorderingAllowed(true);
		setSelectable(true);
		setImmediate(true);
		setNullSelectionAllowed(false);

		addListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				Person person = (Person) event.getProperty().getValue();
				if (person != null) {
					fireViewEvent(ListPresenter.PERSON_SELECTED, person);
				}
			}
		});

		initColumns();

		setSortContainerPropertyId(Person.Fields.firstName.name());
	}

	private void initColumns() {
		setContainerDataSource(new BeanItemContainer<Person>(Person.class));
		setVisibleColumns(NATURAL_COL_ORDER);
		for (Object propertyId : getVisibleColumns()) {
			String header = lang.getText("person-"
					+ String.valueOf(propertyId).toLowerCase());
			setColumnHeader(propertyId, header);
		}

		addGeneratedColumn("email", new ColumnGenerator() {
			@Override
			public Component generateCell(Table source, Object itemId,
					Object columnId) {
				Person p = (Person) itemId;
				Link l = new Link();
				l.setResource(new ExternalResource("mailto:" + p.getEmail()));
				l.setCaption(p.getEmail());
				return l;
			}
		});
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

	private void fireViewEvent(String methodIdentifier,
			Object primaryParameter, Object... secondaryParameters) {
		viewEvent.select(
				new EventQualifierImpl(methodIdentifier, ListView.class) {
				})
				.fire(new ParameterDTO(primaryParameter, secondaryParameters));
	}

	@SuppressWarnings("unchecked")
	public void applyFilter(SearchFilter searchFilter) {
		BeanItemContainer<Person> container = (BeanItemContainer<Person>) getContainerDataSource();
		// clear previous filters
		container.removeAllContainerFilters();
		if (searchFilter != null) {
			// filter contacts with given filter
			container.addContainerFilter(searchFilter.getPropertyId(),
					searchFilter.getTerm(), true, false);

			String propertyName = lang.getText("person-"
					+ String.valueOf(searchFilter.getPropertyId())
							.toLowerCase());
			app.getMainWindow().showNotification(
					"Searched for " + propertyName + "=*"
							+ searchFilter.getTerm() + "*, found "
							+ container.size() + " item(s).",
					Notification.TYPE_TRAY_NOTIFICATION);
		}
	}

}