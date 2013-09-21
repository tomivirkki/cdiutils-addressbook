package org.vaadin.virkki.cdiutils.addressbook.ui.search;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.AbstractPresenter;
import org.vaadin.addon.cdimvp.AbstractPresenter.ViewInterface;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchDAOBean;

@SuppressWarnings("serial")
@ViewInterface(SearchView.class)
public class SearchPresenter extends AbstractPresenter<SearchView> {

    @EJB
    private SearchDAOBean searchDAO;

    /*
     * Every time the SearchView is accessed (MainViewImpl calls
     * SearchViewImpl.openView(); that is) the Presenter is automatically
     * informed, a new SearchFilter is instantiated and passed to the view. View
     * uses the new SearchFilter to re-initialize the search-form.
     */
    @Override
    public void viewEntered() {
        view.editNewSearchFilter(searchDAO.createNew());
    }

}
