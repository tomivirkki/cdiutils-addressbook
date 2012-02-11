package org.vaadin.virkki.cdiutils.addressbook.ui.list;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.virkki.cdiutils.addressbook.data.Person;
import org.vaadin.virkki.cdiutils.addressbook.data.PersonDAOBean;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.main.MainPresenter;
import org.vaadin.virkki.cdiutils.mvp.AbstractPresenter;
import org.vaadin.virkki.cdiutils.mvp.AbstractPresenter.ViewInterface;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;
import org.vaadin.virkki.cdiutils.mvp.View;
import org.vaadin.virkki.cdiutils.mvp.ViewComponent.EventQualifier;

/*
 * List presenter is the presenter of ListView. EJBs and other resources should
 * mainly be accessed in the presenter and results are pushed to the view
 * implementation trough it's API.
 * 
 * If implemented correctly, the presenter imports shouldn't include any
 * com.vaadin... packages (presenter and view implementation are not dependent).
 * Nicest approach would be to have all the presenters, view 
 * implementations(and subcomponents) and interfaces in separate modules.
 * 
 */
@SuppressWarnings("serial")
@ViewInterface(ListView.class)
public class ListPresenter extends AbstractPresenter<ListView> {
	@EJB
	private PersonDAOBean personDAO;

	// CDI Utils includes a built-in CDI event qualifier @EventQualifier which
	// uses a String (method identifier) and a View interface as it's members.
	// All the method id's specific to List-view are located here. Of course you
	// can define your own qualifiers as well but the main idea is to observe
	// events in presenters and fire the events in view-implementations and
	// subcomponents.
	public static final String PERSON_SELECTED = "person_selected";
	public static final String EDIT_PERSON = "edit_person";
	public static final String SAVE_PERSON = "save_person";
	public static final String CANCEL_EDIT = "cancel_edit";

	/*
	 * This method observes events with a ParameterDTO as parameter and an
	 * 
	 * @EventQualifier as qualifier. The method is invoked if the qualifiers
	 * methodId = PERSON_SELECTED and viewInterface = ListView.class.
	 * ListView.class is used because only the presenter of ListView is supposed
	 * to receive an event. If other presenters should also observe an event
	 * (fired from ListViewImpl for example), View.class would be used as the
	 * viewInterface member instead.
	 */
	protected void personSelected(@Observes @EventQualifier(methodId = PERSON_SELECTED, viewInterface = ListView.class) ParameterDTO parameters) {
		view.showSelectedPersonDetails();
	}

	protected void editPerson(@Observes @EventQualifier(methodId = EDIT_PERSON, viewInterface = ListView.class) ParameterDTO parameters) {
		view.editSelectedPerson();
	}

	/*
	 * This method nicely demonstrates how the control logic over views can be
	 * handled in presenters: If the person isn't persistent yet, the view is
	 * told to add the newly persisted person to the navigation tree. Otherwise
	 * the view is only told to set the person selected.
	 */
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

	protected void cancel(@Observes @EventQualifier(methodId = CANCEL_EDIT, viewInterface = ListView.class) ParameterDTO parameters) {
		view.cancelEditing();
	}

	protected void showAll(@Observes @EventQualifier(methodId = MainPresenter.SHOW_ALL, viewInterface = View.class) ParameterDTO parameters) {
		view.setPersonList(personDAO.listPeople());
		view.applyFilter(null);
	}

	/*
	 * The @EventQualifier of this method has viewInterface = View.class as it's
	 * member meaning that some other presenter (MainPresenter) is also
	 * observing the same event. MainPresenter does whatever it does (sets
	 * ListView as currently visible view) but here ListView is told to edit a
	 * newly instantiated contact (Person).
	 */
	protected void newContact(@Observes @EventQualifier(methodId = MainPresenter.NEW_CONTACT, viewInterface = View.class) ParameterDTO parameters) {
		view.editNewContact(personDAO.createNew());
	}

	protected void search(@Observes @EventQualifier(methodId = MainPresenter.SEARCH, viewInterface = View.class) ParameterDTO parameters) {
		SearchFilter searchFilter = parameters.getPrimaryParameter(SearchFilter.class);
		view.applyFilter(searchFilter);
	}

	/*
	 * When the ListPresenter is initialized, it fetches a list of cities from
	 * backend (personDAO) and passes the list to the view.
	 */
	@Override
	protected void initPresenter() {
		view.setCityOptions(personDAO.listCities());
	}

	@Override
	public void viewOpened() {

	}

}
