package ebook.web;

public interface IJetty {

	public String host();

	public String info();

	public void startJetty();

	public String jettyMessage();

	public boolean debug();

}