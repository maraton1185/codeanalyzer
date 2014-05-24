package codeanalyzer.core;

import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import codeanalyzer.auth.SignIn;
import codeanalyzer.core.models.ServiceFactory;
import codeanalyzer.module.books.views.section.tools.SectionComposite;
import codeanalyzer.module.booksList.BookListManager;
import codeanalyzer.module.cf.Cf;
import codeanalyzer.module.cf.CfManager;
import codeanalyzer.module.cf.LoaderManager;
import codeanalyzer.module.cf.services.CfServices;
import codeanalyzer.module.cf.services.TextParser;
import codeanalyzer.module.db.DbConnection;
import codeanalyzer.module.users.UserManager;

public final class pico {

	private pico() {
	}

	private static MutablePicoContainer instance;

	private static MutablePicoContainer Instance() {
		if (!(instance instanceof MutablePicoContainer)) {
			instance = new DefaultPicoContainer();
			init();
		}
		return instance;
	}

	private static void init() {
		instance.as(Characteristics.CACHE).addComponent(SignIn.class);

		instance.as(Characteristics.CACHE).addComponent(DbConnection.class);

		instance.addComponent(Cf.class);
		instance.as(Characteristics.CACHE).addComponent(CfManager.class);
		instance.as(Characteristics.CACHE).addComponent(CfServices.class);

		instance.as(Characteristics.CACHE).addComponent(TextParser.class);
		instance.as(Characteristics.CACHE).addComponent(LoaderManager.class);

		instance.as(Characteristics.CACHE).addComponent(BookListManager.class);
		instance.as(Characteristics.CACHE).addComponent(UserManager.class);

		instance.as(Characteristics.CACHE).addComponent(ServiceFactory.class);

		// instance.as(Characteristics.CACHE).addComponent(BookServices.class);
		// instance.as(Characteristics.CACHE).addComponent(BookService.class);
		// instance.as(Characteristics.CACHE).addComponent(Events.class);
		// instance.as(Characteristics.CACHE).addComponent(EditorFactory.class);
		// instance.as(Characteristics.CACHE).addComponent(History.class);
		//

		// // instance.addComponent(TextParser.class);
		// instance.as(Characteristics.CACHE).addComponent(ColorManager.class);
		// instance.addComponent(CData.class);
		// instance.addComponent(ColorManager.class);
		instance.addComponent(SectionComposite.class);

	}

	public static <T> T get(Class<T> type) {
		return Instance().getComponent(type);
	}
}
