package org.vaadin.virkki.cdiutils.addressbook.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SearchFilter implements Serializable {
    private Long id;
    private String term;
    private Object propertyId;
    private String searchName;

    public enum Fields {
        id, term, propertyId, searchName
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Object getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Object propertyId) {
        this.propertyId = propertyId;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    @Override
    public String toString() {
        return getSearchName();
    }

    public boolean isPersistent() {
        return id != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getId() == null) {
            return obj == this;
        } else {
            return getId().equals(((SearchFilter) obj).getId());
        }
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        } else {
            return super.hashCode();
        }
    }

}
