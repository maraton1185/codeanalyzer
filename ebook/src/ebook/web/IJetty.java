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

	public HashMap<IPath, Connection> pull();

	public void openBookOnStratUp();

	public void setOpenBookOnStratUp();

	// url

	String host();

	String section(Integer book, Integer section);

	String book(Integer book);

	String list(Integer id);

	String list();

	String editor();

	String bookImage(Integer book, Integer id);

	String listImage(Integer book);

	String swt();

}