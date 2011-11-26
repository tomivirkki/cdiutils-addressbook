package org.vaadin.virkki.cdiutils.addressbook.ui.list;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.virkki.cdiutils.addressbook.data.Person;
import org.vaadin.virkki.cdiutils.addressbook.data.PersonDAOBean;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.main.MainPresenter;
import org.vaadin.virkki.cdiutils.mvp.AbstractPresenter;
import org.vaadin.virkki.cdiutils.mvp.AbstractPresenter.ViewInterface;
import org.vaadin.virkki.cdiutils.mvp.AbstractView.EventQualifier;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;
import org.vaadin.virkki.cdiutils.mvp.View;

@SuppressWarnings("serial")
@ViewInterface(ListView.class)
public class ListPresenter extends AbstractPresenter<ListView> {
	@EJB
	private PersonDAOBean personDAO;

	public static final String PERSON_SELECTED = "ps";
	public static final String SAVE_PERSON = "sp";
	public static final String EDIT_PERSON = "ep";
	public static final String CANCEL = "cancel";

	protected void personSelected(@Observes @EventQualifier(methodId = PERSON_SELECTED, viewInterface = ListView.class) ParameterDTO parameters) {
		view.showSelectedPersonDetails();
	}

	protected void editPerson(@Observes @EventQualifier(methodId = EDIT_PERSON, viewInterface = ListView.class) ParameterDTO parameters) {
		view.editSelectedPerson();
	}

	protected void savePerson(@Observes @EventQualifier(methodId = SAVE_PERSON, viewInterface = ListView.class) ParameterDTO parameters) {
		Person person = parameters.getPrimaryParameter(Person.class);
		if (person.isPersistent()) {
			personDAO.update(person);
		} else {
			person = personDAO.persist(person);
			view.addContactToList(person);
		}
		view.selectPerson(person);
	}

	protected void showAll(@Observes @EventQualifier(methodId = MainPresenter.SHOW_ALL, viewInterface = View.class) ParameterDTO parameters) {
		view.setPersonList(personDAO.listPeople());
		view.applyFilter(null);
	}

	protected void newContact(@Observes @EventQualifier(methodId = MainPresenter.NEW_CONTACT, viewInterface = View.class) ParameterDTO parameters) {
		view.editNewContact(personDAO.createNew());
	}

	protected void search(@Observes @EventQualifier(methodId = MainPresenter.SEARCH, viewInterface = View.class) ParameterDTO parameters) {
		SearchFilter searchFilter = parameters.getPrimaryParameter(SearchFilter.class);
		view.applyFilter(searchFilter);
	}

	protected void cancel(@Observes @EventQualifier(methodId = CANCEL, viewInterface = ListView.class) ParameterDTO parameters) {
		view.cancelEditing();
	}

	@Override
	protected void initPresenter() {
		view.setCityOptions(personDAO.listCities());
	}

	@Override
	public void viewOpened() {

	}

}
