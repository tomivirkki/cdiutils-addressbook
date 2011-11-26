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

	}

	@Override
	public void viewOpened() {
		view.editNewSearchFilter(searchDAO.createNew());
	}

}
