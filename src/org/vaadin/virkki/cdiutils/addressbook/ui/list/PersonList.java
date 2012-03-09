package org.vaadin.virkki.cdiutils.addressbook.ui.list;

import java.util.Collection;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.Person;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.componentproducers.Preconfigured;
import org.vaadin.virkki.cdiutils.mvp.ViewComponent;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Window.Notification;

@SuppressWarnings("serial")
@SessionScoped
public class PersonList extends ViewComponent {
	@Inject
	@Preconfigured(nullSelectionAllowed = false, sizeFull = true, immediate = true)
	private Table table;

	public static final Object[] NATURAL_COL_ORDER = new Object[] {
			Person.Fields.firstName.name(), Person.Fields.lastName.name(),
			Person.Fields.email.name(), Person.Fields.phoneNumber.name(),
			Person.Fields.streetAddress.name(),
			Person.Fields.postalCode.name(), Person.Fields.city.name() };

	public void init() {
		setCompositionRoot(table);
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);
		table.setSelectable(true);

		table.addListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				Person person = (Person) event.getProperty().getValue();
				if (person != null) {
					fireViewEvent(ListPresenter.PERSON_SELECTED,
							ListView.class, person);
				}
			}
		});

		initColumns();
	}

	private void initColumns() {
		table.setContainerDataSource(new BeanItemContainer<Person>(Person.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		for (Object propertyId : table.getVisibleColumns()) {
			String header = getText("person-"
					+ String.valueOf(propertyId).toLowerCase());
			table.setColumnHeader(propertyId, header);
		}

		table.addGeneratedColumn(Person.Fields.email.name(),
				new ColumnGenerator() {
					@Override
					public Component generateCell(Table source, Object itemId,
							Object columnId) {
						String email = ((Person) itemId).getEmail();
						return new Link(email, new ExternalResource("mailto:"
								+ email));
					}
				});

		table.setSortContainerPropertyId(Person.Fields.firstName.name());
	}

	public void setPersonList(Collection<Person> people) {
		table.removeAllItems();
		for (Person person : people) {
			table.addItem(person);
		}
		table.sort();
	}

	public void addContactToList(Person person) {
		table.addItem(person);
		table.sort();
	}

	@SuppressWarnings("unchecked")
	public void applyFilter(SearchFilter searchFilter) {
		BeanItemContainer<Person> container = (BeanItemContainer<Person>) table
				.getContainerDataSource();
		// clear previous filters
		container.removeAllContainerFilters();

		if (searchFilter != null) {
			// filter contacts with given filter
			container.addContainerFilter(searchFilter.getPropertyId(),
					searchFilter.getTerm(), true, false);

			String propertyName = getText("person-"
					+ String.valueOf(searchFilter.getPropertyId())
							.toLowerCase());
			/*
			 * personlist-searchnotification -text has 3 parameters which are
			 * passed in the Lang.getText-method.
			 */
			String notificationText = getText("personlist-searchnotification",
					propertyName, searchFilter.getTerm(), container.size());
			applicationWrapper
					.getApplication()
					.getMainWindow()
					.showNotification(notificationText,
							Notification.TYPE_TRAY_NOTIFICATION);
		}
	}

	public void setValue(Person person) {
		table.setValue(person);
	}

	public Item getSelectedItem() {
		return table.getItem(table.getValue());
	}

}