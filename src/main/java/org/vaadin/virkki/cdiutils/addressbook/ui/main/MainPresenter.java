package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchDAOBean;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.ListView;
import org.vaadin.virkki.cdiutils.addressbook.ui.search.SearchView;

@SuppressWarnings("serial")
@ViewInterface(MainViev.class)
public class MainPresenter extends AbstractMVPPresenter<MainViev> {

    @EJB
    private SearchDAOBean searchDAO;

    public static final String SHARE = "main_presenter_share";
    public static final String HELP = "main_presenter_help";
    public static final String SHOW_ALL = "main_presenter_show_all";
    public static final String NEW_SEARCH = "main_presenter_new_search";
    public static final String NEW_CONTACT = "main_presenter_new_contact";
    public static final String SAVE_SEARCH = "main_presenter_save_search";
    public static final String SEARCH = "main_presenter_search";

    protected void shareWindowRequested(
            @Observes @CDIEvent(SHARE) final ParameterDTO parameters) {
        view.showShareWindow();
    }

    protected void helpWindowRequested(
            @Observes @CDIEvent(HELP) final ParameterDTO parameters) {
        view.showHelpWindow();
    }

    protected void showAll(
            @Observes @CDIEvent(SHOW_ALL) final ParameterDTO parameters) {
        view.setView(ListView.class, true);
    }

    protected void newSearch(
            @Observes @CDIEvent(NEW_SEARCH) final ParameterDTO parameters) {
        view.setView(SearchView.class, true);
    }

    protected void newContact(
            @Observes @CDIEvent(NEW_CONTACT) final ParameterDTO parameters) {
        view.setView(ListView.class, true);
    }

    protected void saveSearch(
            @Observes @CDIEvent(SAVE_SEARCH) final ParameterDTO parameters) {
        SearchFilter searchFilter = parameters
                .getPrimaryParameter(SearchFilter.class);
        searchFilter = searchDAO.persist(searchFilter);
        view.addSearchToTree(searchFilter);
    }

    protected void search(
            @Observes @CDIEvent(SEARCH) final ParameterDTO parameters) {
        view.setView(ListView.class, false);
    }

    @Override
    public void viewEntered() {
        // NOP
    }

}
