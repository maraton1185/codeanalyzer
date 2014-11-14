package ebook.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ebook.utils.PreferenceSupplier;
import ebook.web.controllers.EditorController;

public class EbookServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		sc.setAttribute("brand",
				PreferenceSupplier.get(PreferenceSupplier.APP_BRAND));

		// sc.setAttribute("swt", App.getJetty().swt());

		if (!PreferenceSupplier
				.getBoolean(PreferenceSupplier.LOAD_EDITOR_TEMPLATES_ON_GET)) {

			EditorController contrl = new EditorController();
			sc.setAttribute("templates", contrl.getModel());

		}
		;

	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

}
