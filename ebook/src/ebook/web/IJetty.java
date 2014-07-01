package ebook.web;

public interface IJetty {

	public enum JettyStatus {
		started, stopped, error;
	}

	public JettyStatus status();

	public void startJetty();

	public String jettyMessage();

	public void setManual();

	public String host();

	public String info();

	public String book(Integer integer);

}