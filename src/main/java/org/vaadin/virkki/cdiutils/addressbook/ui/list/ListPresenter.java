package org.vaadin.virkki.cdiutils.addressbook.ui.list;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.virkki.cdiutils.addressbook.data.Person;
import org.vaadin.virkki.cdiutils.addressbook.data.PersonDAOBean;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.main.MainPresenter;

/*
 * List presenter is the presenter for ListView. EJBs and other resources should
 * mainly be accessed in the presenter and results are pushed to the view
 * implementation trough it's API.
 */
@SuppressWarnings("serial")
@ViewInterface(ListView.class)
public class ListPresenter extends AbstractMVPPresenter<ListView> {
    @Inject
    private PersonDAOBean personDAO;

    // CDI MVP includes a built-in CDI event qualifier @CDIEvent which
    // uses a String (method identifier) as it's member
    public static final String PERSON_SELECTED = "list_presenter_person_selected";
    public static final String EDIT_PERSON = "list_presenter_edit_person";
    public static final String SAVE_PERSON = "list_presenter_save_person";
    public static final String CANCEL_EDIT = "list_presenter_cancel_edit";

    /*
     * This method observes events with a ParameterDTO as the parameter type and
     * PERSON_SELECTED as the @CDIEvent value
     */
    protected void personSelected(
            @Observes @CDIEvent(PERSON_SELECTED) final ParameterDTO parameters) {
        view.showSelectedPersonDetails();
    }

    protected void editPerson(
            @Observes @CDIEvent(EDIT_PERSON) final ParameterDTO parameters) {
        view.editSelectedPerson();
    }

    /*
     * This method nicely demonstrates how the control logic over views can be
     * handled in presenters: If the person isn't persistent yet, the view is
     * told to add the newly persisted person to the navigation tree. Otherwise
     * the view is only told to set the person selected.
     */
    protected void savePerson(
            @Observes @CDIEvent(SAVE_PERSON) final ParameterDTO parameters) {
        Person person = parameters.getPrimaryParameter(Person.class);
        if (person.isPersistent()) {
            personDAO.update(person);
        } else {
            person = personDAO.persist(person);
            view.addContactToList(person);
        }
        view.selectPerson(person);
    }

    protected void cancel(
            @Observes @CDIEvent(CANCEL_EDIT) final ParameterDTO parameters) {
        view.cancelEditing();
    }

    protected void showAll(
            @Observes @CDIEvent(MainPresenter.SHOW_ALL) final ParameterDTO parameters) {
        view.setPersonList(personDAO.listPeople());
        view.applyFilter(null);
    }

    protected void newContact(
            @Observes @CDIEvent(MainPresenter.NEW_CONTACT) final ParameterDTO parameters) {
        view.editNewContact(personDAO.createNew());
    }

    protected void search(
            @Observes @CDIEvent(MainPresenter.SEARCH) final ParameterDTO parameters) {
        final SearchFilter searchFilter = parameters
                .getPrimaryParameter(SearchFilter.class);
        view.applyFilter(searchFilter);
    }

    /*
     * When the ListPresenter is initialized, it fetches a list of cities from
     * backend (personDAO) and passes the list to the view.
     */
    @PostConstruct
    protected void initPresenter() {
        view.setCityOptions(personDAO.listCities());
    }

    @Override
    public void viewEntered() {
        // NOP
    }

}
