package org.vaadin.virkki.cdiutils.addressbook.ui.search;

import org.vaadin.virkki.cdiutils.addressbook.data.SearchFilter;
import org.vaadin.virkki.cdiutils.mvp.View;

public interface SearchView extends View {

	void editNewSearchFilter(SearchFilter searchFilter);

}
