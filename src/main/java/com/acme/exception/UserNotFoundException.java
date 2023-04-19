package com.acme.exception;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = -3090575328690477167L;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	

}
