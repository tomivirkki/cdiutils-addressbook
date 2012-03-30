package org.vaadin.virkki.cdiutils.addressbook.ui.search;

import javax.ejb.EJB;

import org.vaadin.virkki.cdiutils.addressbook.data.SearchDAOBean;
import org.vaadin.virkki.cdiutils.mvp.AbstractPresenter;
import org.vaadin.virkki.cdiutils.mvp.AbstractPresenter.ViewInterface;

@SuppressWarnings("serial")
@ViewInterface(SearchView.class)
public class SearchPresenter extends AbstractPresenter<SearchView> {

    @EJB
    private SearchDAOBean searchDAO;

    @Override
    protected void initPresenter() {
        // NOP
    }

    /*
     * Every time the SearchView is accessed (MainViewImpl calls
     * SearchViewImpl.openView(); that is) the Presenter is automatically
     * informed, a new SearchFilter is instantiated and passed to the view. View
     * uses the new SearchFilter to re-initialize the search-form.
     */
    @Override
    public final void viewOpened() {
        view.editNewSearchFilter(searchDAO.createNew());
    }

}
