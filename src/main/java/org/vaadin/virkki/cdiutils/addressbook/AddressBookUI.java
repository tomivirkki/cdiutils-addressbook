package org.vaadin.virkki.cdiutils.addressbook;

import java.util.Locale;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.virkki.cdiutils.addressbook.ui.main.MainViewImpl;
import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.addressbook.util.Props;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme(Props.THEME_NAME)
@CDIUI
public class AddressBookUI extends UI {

    @Inject
    private Instance<MainViewImpl> mainView;
    @Inject
    private Lang lang;

    @Inject
    @TextBundleUpdated
    private javax.enterprise.event.Event<ParameterDTO> localizeEvent;

    @Override
    public void setLocale(final Locale locale) {
        lang.setLocale(locale);
        super.setLocale(locale);
        localizeEvent.fire(new ParameterDTO(locale));
    }

    @Override
    protected void init(final VaadinRequest request) {
        setLocale(Lang.EN_US);
        setContent(mainView.get());
        mainView.get().enter();
    }

}
