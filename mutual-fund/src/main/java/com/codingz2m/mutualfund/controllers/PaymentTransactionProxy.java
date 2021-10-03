package com.codingz2m.mutualfund.controllers;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

// ****** MAKING REST CALLS USING FEIGN CLIENT **********

@FeignClient(name = "payment-transaction", url = "${PAYMENT_TRANSACTION_SERVICE_HOST:http://localhost}:8083")
public interface PaymentTransactionProxy {

	@PostMapping("api/v1/payment-transaction/{transaction-date}/{transaction-details}/{amount}/{debit-credit}/{savings-account-id}")
	public void initiatePaymentTransaction(
			@PathVariable(value="transaction-date")String transactionDate, 
			@PathVariable(value="transaction-details") String transactionDetails,  
			@PathVariable(value="amount") double amount,
			@PathVariable(value="debit-credit") String debitOrCredit,
			@PathVariable(value="savings-account-id") UUID savingsAccountId 
			);
}