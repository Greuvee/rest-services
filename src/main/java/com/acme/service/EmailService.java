package com.acme.service;

import com.acme.rest.controller.model.UserModel;

public interface EmailService {

	String sendValidationEmail(UserModel user);
	
}
