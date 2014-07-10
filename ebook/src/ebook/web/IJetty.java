package ebook.web;

import java.sql.Connection;
import java.util.HashMap;

import org.eclipse.core.runtime.IPath;

public interface IJetty {

	public enum JettyStatus {
		started, stopped, error;
	}

	public boolean isStarted();

	public void start();

	public void stop();

	public String jettyMessage();

	public void setManual();

	public String host();

	public String info();

	public String book(Integer book, Integer section);

	public HashMap<IPath, Connection> pull();

	public String image(Integer book, Integer id);

}