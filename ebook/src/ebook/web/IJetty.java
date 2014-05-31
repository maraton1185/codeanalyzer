package ebook.web;

public interface IJetty {

	public enum JettyStatus {
		started, stopped, error;
	}

	public JettyStatus status();

	public String host();

	public String info();

	public void startJetty();

	public String jettyMessage();

	public void setManual();

}