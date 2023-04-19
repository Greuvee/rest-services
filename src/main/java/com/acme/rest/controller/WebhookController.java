package com.acme.rest.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.integration.payment.stripe.TransactionDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebhookController.class); 
	
	@Value("${integration.payment.stripe.webhook.endpoint.secret}")
	private String stripeWebhookEndpointSecret;
	
	@Value("${integration.payment.stripe.api.private.key}")
    private static void setStripeApiPrivateKey(String apiKey){
		Stripe.apiKey = apiKey;
    }
	
	private Map<String, String> responseMap = new HashMap<>();
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	
	
	@PostMapping("/stripe")
	public void stripeListener(HttpServletRequest request, @RequestBody String payload) {
		
		try {
			Event event = Webhook.constructEvent(payload, request.getHeader("Stripe-Signature"), stripeWebhookEndpointSecret);
			
			EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            
			Optional<StripeObject> stripeObjectLookup = dataObjectDeserializer.getObject();
			
            if (stripeObjectLookup.isPresent()) {
            	StripeObject stripeObject = stripeObjectLookup.get();
            	
            	String rawJson = stripeObject.toJson();
            	
            	try {
					TransactionDetails txnDetails = objectMapper.readValue(rawJson, TransactionDetails.class);
					LOGGER.info(txnDetails.getClientReferenceId());
				} catch (JsonProcessingException e) {
					LOGGER.error("Exception convertion json to object", e);
				}
            	
//            	if("setup_intent.created".equals(event.getType()) || "setup_intent.succeeded".equals(event.getType())) {
//            		SetupIntent eventData = (SetupIntent) stripeObject;
//            		logMetadata(eventData.getMetadata());
//            	}else if("checkout.session.completed".equals(event.getType())) {
//            		Session eventData = (Session) stripeObject;
//            		logMetadata(eventData.getMetadata());
//            	}else if("customer.created".equals(event.getType()) || "customer.updated".equals(event.getType())) {
//            		Customer eventData = (Customer)stripeObject;
//            		logMetadata(eventData.getMetadata());
//            	}else if("payment_method.attached".equals(event.getType())) {
//            		PaymentMethod eventData = (PaymentMethod) stripeObject;
//            		logMetadata(eventData.getMetadata());
//            	}else if("invoice.created".equals(event.getType()) || "invoice.finalized".equals(event.getType()) || "invoice.paid".equals(event.getType()) || "invoice.payment_succeeded".equals(event.getType())) {
//            		Invoice eventData = (Invoice)stripeObject;
//            		logMetadata(eventData.getMetadata());
//            	}else if("customer.subscription.created".equals(event.getType())) {
//            		Subscription eventData = (Subscription)stripeObject;
//            		logMetadata(eventData.getMetadata());
//            	}else {
//            		LOGGER.warn("No event handler impelemented for event {} ({})", event.getType(), stripeObject.getClass());
//            	}
            } else {
                LOGGER.error("Stripe object is not present");
            }
		} catch (SignatureVerificationException e) {
			LOGGER.error("Stripe webhook error", e);
		}
		
	}
	
	private void logMetadata(Map<String, String> metadata) {
		if(metadata != null && !metadata.isEmpty()) {
			for(Map.Entry<String, String> entry : metadata.entrySet()) {
				LOGGER.info("  -- metadata -- Key={}  Value={}", entry.getKey(), entry.getValue());
			}
		}
	}

}
