package com.acme.service;

import java.io.StringWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.acme.rest.controller.model.UserModel;

import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotNull;

@Service
public class EmailServiceImpl implements EmailService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private Configuration configuration;
	
	@Value("${app.acme.host}")
	private String appHost;
	
	@Value("${send.validation.email.enabled}")
	private boolean sendEmailEnabled;
	
	private static final String NO_REPLY = "noreply@acme.com";
	
	public static final String EMAIL_VALIDATION_FTL = "email-validation.ftl";
	
	@Override
	public String sendValidationEmail(@NotNull UserModel userModel) {
		
        try {
        	String urlEncodedToken = Base64.getUrlEncoder().encodeToString(userModel.getUuid().getBytes());
    		
            if(sendEmailEnabled) {
            	StringWriter stringWriter = new StringWriter();
                Map<String, Object> model = new HashMap<>();
                model.put("appHost", appHost);
                model.put("validationToken", urlEncodedToken);
                model.put("user", userModel);
                configuration.getTemplate(EMAIL_VALIDATION_FTL).process(model, stringWriter);
                
            	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            	MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            	mimeMessageHelper.setSubject("Acme Email Address Validation");
            	mimeMessageHelper.setTo(userModel.getEmailAddress());
            	mimeMessageHelper.setFrom(NO_REPLY);
                mimeMessageHelper.setText(stringWriter.getBuffer().toString(), true);
                
            	javaMailSender.send(mimeMessage);
	        }else {
	        	LOGGER.warn("Sending validation email is DISABLED");
	        }
        
			 return urlEncodedToken;
			 
		} catch (Exception e ) {
			LOGGER.error(e.getMessage(), e);
			return "ERROR";
		}
	}
}
