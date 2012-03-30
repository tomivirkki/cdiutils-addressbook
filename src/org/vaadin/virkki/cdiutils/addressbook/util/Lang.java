package org.vaadin.virkki.cdiutils.addressbook.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.TextBundle;
import org.vaadin.virkki.cdiutils.addressbook.AddressBookApplication;
import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped;
import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped.VaadinScope;

@SuppressWarnings("serial")
@VaadinScoped(VaadinScope.APPLICATION)
public class Lang implements Serializable, TextBundle {
    public static final Locale EN_US = new Locale("en", "US");

    @Inject
    private Instance<AddressBookApplication> application;

    private ResourceBundle resourceBundle;

    @Override
    public final String getText(final String key, final Object... params) {
        String value;
        if (resourceBundle == null) {
            value = "No bundle!";
        } else {
            try {
                value = MessageFormat.format(resourceBundle.getString(key),
                        params);
            } catch (final MissingResourceException e) {
                value = "!" + key;
            }
        }
        return value;
    }

    public final void setLocale(final Locale locale) {
        try {
            resourceBundle = ResourceBundle.getBundle(
                    Props.LANG_RESOURCES_NAME, locale);
        } catch (final MissingResourceException e) {
            // NOP
        }
    }

}
