package ebook.core.exceptions;

public class DbLicenseException extends Exception {

	private static final long serialVersionUID = 4555098102309242996L;

	@Override
	public String getMessage() {
		return "Free-лицензия не позволяет открыть конфигурацию.";
	}

}
