package org.vaadin.virkki.cdiutils.addressbook.util;

import java.util.Locale;

import com.vaadin.data.util.converter.StringToIntegerConverter;

@SuppressWarnings("serial")
public class AddressBookStringToIntConverter extends StringToIntegerConverter {
    @Override
    public String convertToPresentation(Integer value,
            Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return String.valueOf(value);
    }

}
