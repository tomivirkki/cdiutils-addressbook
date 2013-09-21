package org.vaadin.virkki.cdiutils.addressbook.ui.list;

import java.util.Collection;

import org.vaadin.addon.cdimvp.View;
import org.vaadin.virkki.cdiutils.addressbook.data.Person;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;

public interface ListView extends View {

    void setPersonList(Collection<Person> people);

    void showSelectedPersonDetails();

    void editNewContact(Person person);

    void setCityOptions(Collection<String> cityOptions);

    void addContactToList(Person person);

    void selectPerson(Person person);

    void editSelectedPerson();

    void applyFilter(SearchFilter searchFilter);

    void cancelEditing();
}
