package com.acme.exception;

public class UserSecurityException extends Exception {

	private static final long serialVersionUID = -1940945324473531068L;

	public UserSecurityException(String message) {
		super(message);
	}

	public UserSecurityException(Throwable cause) {
		super(cause);
	}

	public UserSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserSecurityException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
