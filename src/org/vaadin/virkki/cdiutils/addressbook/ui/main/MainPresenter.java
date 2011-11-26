package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.virkki.cdiutils.addressbook.data.SearchDAOBean;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.addressbook.ui.list.ListView;
import org.vaadin.virkki.cdiutils.addressbook.ui.search.SearchView;
import org.vaadin.virkki.cdiutils.mvp.AbstractPresenter;
import org.vaadin.virkki.cdiutils.mvp.AbstractPresenter.ViewInterface;
import org.vaadin.virkki.cdiutils.mvp.AbstractView.EventQualifier;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;
import org.vaadin.virkki.cdiutils.mvp.View;

@SuppressWarnings("serial")
@ViewInterface(MainViev.class)
public class MainPresenter extends AbstractPresenter<MainViev> {

	@EJB
	private SearchDAOBean searchDAO;

	public static final String SHARE_WINDOW_REQUESTED = "sr";
	public static final String HELP_WINDOW_REQUESTED = "hr";
	public static final String SHOW_ALL = "sa";
	public static final String NEW_SEARCH = "ns";
	public static final String NEW_CONTACT = "nc";
	public static final String SAVE_SEARCH = "ss";
	public static final String SEARCH = "search";

	protected void shareWindowRequested(
			@Observes @EventQualifier(methodId = SHARE_WINDOW_REQUESTED, viewInterface = View.class) ParameterDTO parameters) {
		view.showShareWindow();
	}

	protected void helpWindowRequested(
			@Observes @EventQualifier(methodId = HELP_WINDOW_REQUESTED, viewInterface = View.class) ParameterDTO parameters) {
		view.showHelpWindow();
	}

	protected void showAll(
			@Observes @EventQualifier(methodId = SHOW_ALL, viewInterface = View.class) ParameterDTO parameters) {
		view.setView(ListView.class, true);
	}

	protected void newSearch(
			@Observes @EventQualifier(methodId = NEW_SEARCH, viewInterface = View.class) ParameterDTO parameters) {
		view.setView(SearchView.class, true);
	}

	protected void newContact(
			@Observes @EventQualifier(methodId = NEW_CONTACT, viewInterface = View.class) ParameterDTO parameters) {
		view.setView(ListView.class, true);
	}

	protected void saveSearch(
			@Observes @EventQualifier(methodId = SAVE_SEARCH, viewInterface = SearchView.class) ParameterDTO parameters) {
		SearchFilter searchFilter = parameters
				.getPrimaryParameter(SearchFilter.class);
		boolean saveSearch = parameters.getSecondaryParameter(0, Boolean.class);

		if (saveSearch) {
			searchFilter = searchDAO.persist(searchFilter);
			view.addSearchToTree(searchFilter);
		}
	}

	protected void search(
			@Observes @EventQualifier(methodId = SEARCH, viewInterface = View.class) ParameterDTO parameters) {
		view.setView(ListView.class, false);
	}

	@Override
	protected void initPresenter() {

	}

	@Override
	public void viewOpened() {

	}

}
