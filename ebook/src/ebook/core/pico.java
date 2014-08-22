package ebook.core;

import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import ebook.auth.SignIn;
import ebook.core.models.ManagerFactory;
import ebook.core.models.ServiceFactory;
import ebook.module.confLoad.LoaderManager;
import ebook.module.confLoad.services.CfServices;
import ebook.module.db.DbConnection;
import ebook.module.text.model.ColorManager;
import ebook.module.tree.Clipboard;

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

		instance.as(Characteristics.CACHE).addComponent(CfServices.class);

		instance.as(Characteristics.CACHE).addComponent(LoaderManager.class);

		instance.as(Characteristics.CACHE).addComponent(ServiceFactory.class);
		instance.as(Characteristics.CACHE).addComponent(ManagerFactory.class);

		instance.as(Characteristics.CACHE).addComponent(ebook.web.Jetty.class);

		instance.as(Characteristics.CACHE).addComponent(Clipboard.class);

		instance.as(Characteristics.CACHE).addComponent(ColorManager.class);

	}

	public static <T> T get(Class<T> type) {
		return Instance().getComponent(type);
	}
}
