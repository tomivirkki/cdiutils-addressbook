package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.mvp.View;

public interface MainViev extends View {

	void setView(Class<? extends View> viewClass, boolean selectInNavigationTree);

	void showHelpWindow();

	void showShareWindow();

	void addSearchToTree(SearchFilter searchFilter);
}
