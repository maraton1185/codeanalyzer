package ebook.core;

import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import ebook.auth.SignIn;
import ebook.core.models.ManagerFactory;
import ebook.core.models.ServiceFactory;
import ebook.module.book.BookClipboard;
import ebook.module.confLoad.LoaderManager;
import ebook.module.confLoad.services.CfServices;
import ebook.module.db.DbConnection;

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

		// instance.addComponent(Cf.class);
		// instance.as(Characteristics.CACHE).addComponent(ConfManager.class);
		instance.as(Characteristics.CACHE).addComponent(CfServices.class);

		// instance.as(Characteristics.CACHE).addComponent(TextParser.class);
		instance.as(Characteristics.CACHE).addComponent(LoaderManager.class);

		// instance.as(Characteristics.CACHE).addComponent(BookListManager.class);
		// instance.as(Characteristics.CACHE).addComponent(UserManager.class);
		// instance.as(Characteristics.CACHE).addComponent(ConfManager.class);

		instance.as(Characteristics.CACHE).addComponent(ServiceFactory.class);
		instance.as(Characteristics.CACHE).addComponent(ManagerFactory.class);

		instance.as(Characteristics.CACHE).addComponent(ebook.web.Jetty.class);

		instance.as(Characteristics.CACHE).addComponent(BookClipboard.class);

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
		// instance.addComponent(NotUsedSectionComposite.class);

	}

	public static <T> T get(Class<T> type) {
		return Instance().getComponent(type);
	}
}
