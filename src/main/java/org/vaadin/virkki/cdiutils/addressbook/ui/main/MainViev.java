package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import org.vaadin.addon.cdimvp.View;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;

public interface MainViev extends View {

    void setView(Class<? extends View> viewClass, boolean selectInNavigationTree);

    void showHelpWindow();

    void showShareWindow();

    void addSearchToTree(SearchFilter searchFilter);
}
