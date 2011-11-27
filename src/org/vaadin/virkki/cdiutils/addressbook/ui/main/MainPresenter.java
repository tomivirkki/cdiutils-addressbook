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

	public static final String SHARE = "share";
	public static final String HELP = "help";
	public static final String SHOW_ALL = "show_all";
	public static final String NEW_SEARCH = "new_search";
	public static final String NEW_CONTACT = "new_contact";
	public static final String SAVE_SEARCH = "save_search";
	public static final String SEARCH = "search";

	protected void shareWindowRequested(@Observes @EventQualifier(methodId = SHARE, viewInterface = View.class) ParameterDTO parameters) {
		view.showShareWindow();
	}

	protected void helpWindowRequested(@Observes @EventQualifier(methodId = HELP, viewInterface = View.class) ParameterDTO parameters) {
		view.showHelpWindow();
	}

	protected void showAll(@Observes @EventQualifier(methodId = SHOW_ALL, viewInterface = View.class) ParameterDTO parameters) {
		view.setView(ListView.class, true);
	}

	protected void newSearch(@Observes @EventQualifier(methodId = NEW_SEARCH, viewInterface = View.class) ParameterDTO parameters) {
		view.setView(SearchView.class, true);
	}

	protected void newContact(@Observes @EventQualifier(methodId = NEW_CONTACT, viewInterface = View.class) ParameterDTO parameters) {
		view.setView(ListView.class, true);
	}

	protected void saveSearch(@Observes @EventQualifier(methodId = SAVE_SEARCH, viewInterface = View.class) ParameterDTO parameters) {
		SearchFilter searchFilter = parameters.getPrimaryParameter(SearchFilter.class);
		searchFilter = searchDAO.persist(searchFilter);
		view.addSearchToTree(searchFilter);
	}

	protected void search(@Observes @EventQualifier(methodId = SEARCH, viewInterface = View.class) ParameterDTO parameters) {
		view.setView(ListView.class, false);
	}

	@Override
	protected void initPresenter() {

	}

	@Override
	public void viewOpened() {

	}

}
