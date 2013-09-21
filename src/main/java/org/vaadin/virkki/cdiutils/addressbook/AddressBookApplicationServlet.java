package org.vaadin.virkki.cdiutils.addressbook;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import org.vaadin.virkki.cdiutils.addressbook.util.Props;

import com.vaadin.server.Constants;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;

@WebServlet(urlPatterns = "/*", initParams = {
        @WebInitParam(name = VaadinSession.UI_PARAMETER, value = Props.UI_NAME),
        @WebInitParam(name = Constants.SERVLET_PARAMETER_UI_PROVIDER, value = "com.vaadin.cdi.CDIUIProvider") })
public class AddressBookApplicationServlet extends VaadinServlet {
}
