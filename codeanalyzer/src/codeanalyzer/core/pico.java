package codeanalyzer.core;

import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import codeanalyzer.auth.SignIn;


public final class pico {

	private pico() {
    }
	
	private static MutablePicoContainer instance;
	
	private static MutablePicoContainer Instance()
	{
		if (!(instance instanceof MutablePicoContainer)) {
			instance = new DefaultPicoContainer();
			init();
		}
		return instance;
	}
	
	private static void init() {
		instance.as(Characteristics.CACHE).addComponent(SignIn.class);
//		instance.as(Characteristics.CACHE).addComponent(DbManager.class);
//		instance.as(Characteristics.CACHE).addComponent(LoaderService.class);
//		instance.as(Characteristics.CACHE).addComponent(Events.class);
//		instance.as(Characteristics.CACHE).addComponent(EditorFactory.class);
//		instance.as(Characteristics.CACHE).addComponent(History.class);
//		
//		instance.as(Characteristics.CACHE).addComponent(TextParser.class);
////		instance.addComponent(TextParser.class);
//		instance.as(Characteristics.CACHE).addComponent(ColorManager.class);
//		instance.addComponent(CData.class);
//		instance.addComponent(ColorManager.class);
		
//		instance.addComponent(Db.class);
		

	}

	public static <T> T get(Class<T> type)
	{
		return Instance().getComponent(type);
	}
}
