package org.vaadin.virkki.cdiutils.addressbook.ui.search;

import org.vaadin.addon.cdimvp.MVPView;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;

public interface SearchView extends MVPView {

    void editNewSearchFilter(SearchFilter searchFilter);

}
