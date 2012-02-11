package org.vaadin.virkki.cdiutils.addressbook.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.vaadin.virkki.cdiutils.TextBundle;
import org.vaadin.virkki.cdiutils.application.ApplicationWrapper;

@SuppressWarnings("serial")
@SessionScoped
public class Lang implements Serializable, TextBundle {
	public static final Locale en_US = new Locale("en", "US");

	@Inject
	private ApplicationWrapper applicationWrapper;

	private ResourceBundle resourceBundle;

	@Override
	public String getText(String key, Object... params) {
		try {
			String value = resourceBundle.getString(key);
			return MessageFormat.format(value, params);
		} catch (MissingResourceException e) {
			return "!" + key;
		} catch (NullPointerException e) {
			return "No bundle!";
		}
	}

	public void setLocale(Locale locale) {
		try {
			resourceBundle = ResourceBundle.getBundle(Props.LANG_RESOURCES_NAME, locale);
			applicationWrapper.getApplication().setLocale(locale);
		} catch (MissingResourceException e) {
			// NOP
		}
	}

}
