package ebook.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ebook.core.App;
import ebook.utils.PreferenceSupplier;

public class EbookServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		sc.setAttribute("brand",
				PreferenceSupplier.get(PreferenceSupplier.APP_BRAND));
		sc.setAttribute("root_url", App.getJetty().list());
		sc.setAttribute("login_url", App.getJetty().login());
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub

	}

}
