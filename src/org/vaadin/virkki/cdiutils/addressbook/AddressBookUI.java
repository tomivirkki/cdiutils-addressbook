package org.vaadin.virkki.cdiutils.addressbook;

import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import org.vaadin.virkki.cdiutils.addressbook.ui.main.MainViewImpl;
import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.addressbook.util.Props;
import org.vaadin.virkki.cdiutils.application.CdiApplicationServlet;
import org.vaadin.virkki.cdiutils.application.VaadinContext.VaadinScoped;

import com.vaadin.annotations.Theme;
import com.vaadin.terminal.WrappedRequest;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme(Props.THEME_NAME)
@VaadinScoped
public class AddressBookUI extends UI {

    @WebServlet(urlPatterns = "/*", initParams = @WebInitParam(name = "UI", value = "org.vaadin.virkki.cdiutils.addressbook.AddressBookUI"))
    public static class AddressBookApplicationServlet extends
            CdiApplicationServlet {
    }

    @Inject
    private MainViewImpl mainView;
    @Inject
    private Lang lang;

    @Override
    public void setLocale(final Locale locale) {
        lang.setLocale(Lang.EN_US);
        super.setLocale(locale);
    }

    @Override
    protected void init(final WrappedRequest request) {
        setLocale(Lang.EN_US);
        setContent(mainView);
        mainView.openView();
    }

}
