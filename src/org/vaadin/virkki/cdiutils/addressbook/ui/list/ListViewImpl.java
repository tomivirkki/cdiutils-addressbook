package org.vaadin.virkki.cdiutils.addressbook.ui.list;

import java.util.Collection;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.Person;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.mvp.AbstractView;

import com.vaadin.data.Item;
import com.vaadin.ui.VerticalSplitPanel;

@SuppressWarnings("serial")
public class ListViewImpl extends AbstractView implements ListView {
	@Inject
	private Instance<PersonForm> personForm;
	@Inject
	private Instance<PersonList> personList;

	@Override
	protected void initView() {
		addStyleName("view");
		setSizeFull();

		VerticalSplitPanel mainPanel = new VerticalSplitPanel();
		setCompositionRoot(mainPanel);

		personList.get().init();
		mainPanel.setFirstComponent(personList.get());

		personForm.get().init();
		mainPanel.setSecondComponent(personForm.get());
		mainPanel.setSplitPosition(40);
	}

	@Override
	public void setPersonList(Collection<Person> people) {
		personList.get().setPersonList(people);
	}

	@Override
	public void showSelectedPersonDetails() {
		Item item = personList.get().getItem(personList.get().getValue());
		personForm.get().setItemDataSource(item);
		personForm.get().setReadOnly(true);
	}

	@Override
	public void editNewContact(Person person) {
		personList.get().setValue(null);
		personForm.get().editNewContact(person);
	}

	@Override
	public void setCityOptions(Collection<String> cityOptions) {
		personForm.get().setCityOptions(cityOptions);
	}

	@Override
	public void addContactToList(Person person) {
		personList.get().addContactToList(person);
	}

	@Override
	public void selectPerson(Person person) {
		personList.get().setValue(person);
		personForm.get().setReadOnly(true);
	}

	@Override
	public void editSelectedPerson() {
		personForm.get().setReadOnly(false);
	}

	@Override
	public void applyFilter(SearchFilter searchFilter) {
		personList.get().applyFilter(searchFilter);
	}
}