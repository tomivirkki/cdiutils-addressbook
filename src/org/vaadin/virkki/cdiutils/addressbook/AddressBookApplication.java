package org.vaadin.virkki.cdiutils.addressbook;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

import org.vaadin.virkki.cdiutils.addressbook.ui.main.MainViewImpl;
import org.vaadin.virkki.cdiutils.addressbook.util.Lang;
import org.vaadin.virkki.cdiutils.addressbook.util.Props;
import org.vaadin.virkki.cdiutils.application.AbstractCdiApplication;
import org.vaadin.virkki.cdiutils.application.AbstractCdiApplicationServlet;

import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class AddressBookApplication extends AbstractCdiApplication {

	@WebServlet(urlPatterns = "/*")
	public static class AddressBookApplicationServlet extends AbstractCdiApplicationServlet {
	}

	@Inject
	private Instance<MainViewImpl> mainView;
	@Inject
	private Instance<Lang> lang;

	@Override
	public void init() {
		if (mainView.get().getParent()==null){
			lang.get().setLocale(Lang.en_US);
			setMainWindow(new Window(lang.get().getText("mainwindow-name")));
			setTheme(Props.THEME_NAME);
			getMainWindow().setContent(mainView.get());
			mainView.get().openView();
		}
	}
}
