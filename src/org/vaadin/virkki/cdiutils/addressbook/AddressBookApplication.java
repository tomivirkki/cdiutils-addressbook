package org.vaadin.virkki.cdiutils.addressbook;

import java.util.Locale;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import org.vaadin.virkki.cdiutils.addressbook.ui.main.MainViewImpl;
import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.addressbook.util.Props;
import org.vaadin.virkki.cdiutils.application.AbstractCdiApplication;
import org.vaadin.virkki.cdiutils.application.CdiApplicationServlet;

import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class AddressBookApplication extends AbstractCdiApplication {

    @WebServlet(urlPatterns = "/*", initParams = @WebInitParam(name = "application", value = "org.vaadin.virkki.cdiutils.addressbook.AddressBookApplication"))
    public static class AddressBookApplicationServlet extends
            CdiApplicationServlet {
    }

    @Inject
    private Instance<MainViewImpl> mainView;
    @Inject
    private Instance<Lang> lang;

    @Override
    public final void init() {
        setLocale(Lang.EN_US);
        setTheme(Props.THEME_NAME);
        setMainWindow(new Window(lang.get().getText("mainwindow-name")));
        getMainWindow().setContent(mainView.get());
        mainView.get().openView();
    }

    @Override
    public final void setLocale(final Locale locale) {
        lang.get().setLocale(Lang.EN_US);
        super.setLocale(locale);
    }

    // The following methods are for multi-window support
    @Override
    protected final Window instantiateNewWindowIfNeeded(final String name) {
        return new Window(getMainWindow().getCaption());
    }

    @Override
    protected final void buildNewWindow(final Window newWindow) {
        newWindow.setContent(mainView.get());
        mainView.get().openView();
    }

}
