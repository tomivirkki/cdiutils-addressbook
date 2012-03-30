package org.vaadin.virkki.cdiutils.addressbook.ui.list;

import java.util.Collection;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.addressbook.data.Person;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.mvp.AbstractView;

import com.vaadin.data.Item;
import com.vaadin.ui.VerticalSplitPanel;

/*
 * ListViewImpl is the implementation of ListView and receives
 * the calls from ListPresenter.
 */
@SuppressWarnings("serial")
public class ListViewImpl extends AbstractView implements ListView {
    /*
     * Instance<PersonForm> is used here so the personForm and personList won't
     * be injected until needed (Lazy initialization). They are both
     * VaadinScoped(VaadinScope.WINDOW) so personForm.get() will always return
     * the same instance (for the current application level window).
     */
    @Inject
    private Instance<PersonForm> personForm;
    @Inject
    private Instance<PersonList> personList;

    /*
     * View is constructed in the initView-method.
     */
    @Override
    protected final void initView() {
        addStyleName("view");
        setSizeFull();

        final VerticalSplitPanel mainPanel = new VerticalSplitPanel();
        setCompositionRoot(mainPanel);

        personList.get().init();
        mainPanel.setFirstComponent(personList.get());

        personForm.get().init();
        mainPanel.setSecondComponent(personForm.get());
        mainPanel.setSplitPosition(40);
    }

    @Override
    public final void setPersonList(final Collection<Person> people) {
        personList.get().setPersonList(people);
    }

    @Override
    public final void showSelectedPersonDetails() {
        final Item item = personList.get().getSelectedItem();
        personForm.get().setItemDataSource(item);
        personForm.get().setReadOnly(true);
    }

    @Override
    public final void editNewContact(final Person person) {
        personList.get().setValue(null);
        personForm.get().editNewContact(person);
    }

    @Override
    public final void setCityOptions(final Collection<String> cityOptions) {
        personForm.get().setCityOptions(cityOptions);
    }

    @Override
    public final void addContactToList(final Person person) {
        personList.get().addContactToList(person);
    }

    @Override
    public final void selectPerson(final Person person) {
        personList.get().setValue(person);
        personForm.get().setReadOnly(true);
    }

    @Override
    public final void editSelectedPerson() {
        personForm.get().setReadOnly(false);
    }

    @Override
    public final void applyFilter(final SearchFilter searchFilter) {
        personList.get().applyFilter(searchFilter);
    }

    @Override
    public final void cancelEditing() {
        personForm.get().cancel();
    }
}