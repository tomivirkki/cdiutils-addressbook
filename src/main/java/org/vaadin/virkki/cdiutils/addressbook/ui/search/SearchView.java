package org.vaadin.virkki.cdiutils.addressbook.ui.search;

import org.vaadin.addon.cdimvp.View;
import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;

public interface SearchView extends View {

    void editNewSearchFilter(SearchFilter searchFilter);

}
