package org.vaadin.virkki.cdiutils.addressbook.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.vaadin.virkki.cdiutils.TextBundle;

import com.vaadin.cdi.UIScoped;

@SuppressWarnings("serial")
@UIScoped
public class Lang implements Serializable, TextBundle {
    public static final Locale EN_US = new Locale("en", "US");
    public static final Locale FI_FI = new Locale("fi", "FI");

    private ResourceBundle resourceBundle;

    @Override
    public String getText(final String key, final Object... params) {
        String value;
        if (resourceBundle == null) {
            value = "No bundle!";
        } else if (key == null) {
            value = "Null key!";
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

    public void setLocale(final Locale locale) {
        try {
            resourceBundle = ResourceBundle.getBundle(
                    Props.LANG_RESOURCES_NAME, locale);
        } catch (final MissingResourceException e) {
            // NOP
        }
    }
}
