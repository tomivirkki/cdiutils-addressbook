package org.vaadin.virkki.cdiutils.addressbook.data;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;

@Stateless
public class SearchDAOBean {
    private final Logger log = Logger.getLogger(getClass().getName());

    private static Set<SearchFilter> searchDatabase = new HashSet<SearchFilter>();

    public SearchFilter persist(SearchFilter searchFilter) {
        searchFilter.setId(new Random().nextLong());
        searchDatabase.add(searchFilter);
        log.info("SearchFilter (" + searchFilter.getId() + ") was persisted.");
        return searchFilter;
    }

    public SearchFilter createNew() {
        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setPropertyId(Person.Fields.lastName.name());
        log.info("New searchfilter initialized");
        return searchFilter;
    }

}