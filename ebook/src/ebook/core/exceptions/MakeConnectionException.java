package ebook.core.exceptions;

public class MakeConnectionException extends Exception {

	private static final long serialVersionUID = 4555098102309242996L;

	@Override
	public String getMessage() {
		return "Не удалось установить соединение.";
	}

}
