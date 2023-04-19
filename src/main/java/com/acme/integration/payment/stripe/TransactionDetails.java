package com.acme.integration.payment.stripe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDetails {

	
	private String customerId;
	private String clientReferenceId;

	@JsonProperty("client_reference_id")
	public String getClientReferenceId() {
		return clientReferenceId;
	}

	@JsonProperty("client_reference_id")
	public void setClientReferenceId(String clientReferenceId) {
		this.clientReferenceId = clientReferenceId;
	}

	@JsonProperty("customer")
	public String getCustomerId() {
		return customerId;
	}

	@JsonProperty("customer")
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
