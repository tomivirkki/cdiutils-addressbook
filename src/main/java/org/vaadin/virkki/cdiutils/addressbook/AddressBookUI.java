package org.vaadin.virkki.cdiutils.addressbook;

import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import org.vaadin.virkki.cdiutils.addressbook.ui.main.MainViewImpl;
import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.addressbook.util.Props;
import org.vaadin.virkki.cdiutils.application.CdiUIProvider;
import org.vaadin.virkki.cdiutils.application.UIContext.UIScoped;
import org.vaadin.virkki.cdiutils.componentproducers.Localizer;
import org.vaadin.virkki.cdiutils.mvp.CDIEvent;
import org.vaadin.virkki.cdiutils.mvp.ParameterDTO;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Constants;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme(Props.THEME_NAME)
@UIScoped
public class AddressBookUI extends UI {

    @WebServlet(urlPatterns = "/*", initParams = {
            @WebInitParam(name = VaadinSession.UI_PARAMETER, value = Props.UI_NAME),
            @WebInitParam(name = Constants.SERVLET_PARAMETER_UI_PROVIDER, value = CdiUIProvider.PROVIDER_NAME) })
    public static class AddressBookApplicationServlet extends VaadinServlet {
    }

    @Inject
    private MainViewImpl mainView;
    @Inject
    private Lang lang;

    @Inject
    @CDIEvent(Localizer.UPDATE_LOCALIZED_VALUES)
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
        setContent(mainView);
        mainView.openView();
    }

}
