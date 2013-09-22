package org.vaadin.virkki.cdiutils.addressbook.ui.main;

import org.vaadin.addon.cdimvp.MVPView;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;

public interface MainViev extends MVPView {

    void setView(Class<? extends MVPView> viewClass,
            boolean selectInNavigationTree);

    void showHelpWindow();

    void showShareWindow();

    void addSearchToTree(SearchFilter searchFilter);
}
