package ebook.core.exceptions;

import ebook.utils.Const;

public class CryptException extends Exception {

	private static final long serialVersionUID = -4282279740926844648L;

	public String message = Const.ERROR_CRYPT; 
	
	public CryptException() {
		super();
	}

	public CryptException(String message) {
		super(message);
	}

}
