package ru.configviewer.core;

import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;




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
		instance.as(Characteristics.CACHE).addComponent(EditorFactory.class);	
		instance.as(Characteristics.CACHE).addComponent(Service.class);
		
	}

	public static <T> T get(Class<T> type)
	{
		return Instance().getComponent(type);
	}
}
