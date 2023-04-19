package com.acme.exception;

public class MeetingRequestException extends Exception {

	private static final long serialVersionUID = -3090575328690477167L;

	public MeetingRequestException() {
		super();
	}

	public MeetingRequestException(String message) {
		super(message);
	}

	public MeetingRequestException(Throwable cause) {
		super(cause);
	}

	public MeetingRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	

}
