package com.acme.rest.controller.model.response;

public class UpdatePasswordResponse {

	private boolean updated;
	
	private String message;
	
	public UpdatePasswordResponse() {
		super();
	}
	
	public UpdatePasswordResponse(boolean updated) {
		this.updated = updated;
	}

	public UpdatePasswordResponse(boolean updated, String message) {
		this.updated = updated;
		this.message = message;
	}
	
	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
