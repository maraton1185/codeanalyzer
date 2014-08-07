package ebook.core.exceptions;


public class GetRootException extends Exception {

	private static final long serialVersionUID = -4282279740926844648L;

	public String message = "";

	public GetRootException() {
		super();
	}

	public GetRootException(String message) {
		super(message);
	}

}
