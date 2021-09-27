package com.codingz2m.paymenttransaction.controllers;

import java.awt.PageAttributes.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingz2m.paymenttransaction.data.PaymentTransaction;
import com.codingz2m.paymenttransaction.services.PaymentTransactionService;
import com.codingz2m.paymenttransaction.shared.PaymentTransactionDTO;
import com.codingz2m.paymenttransaction.ui.models.PaymentTransactionResponse;


@RestController
@RequestMapping("api/v1/payment-transaction")
public class PaymentTransactionController {

	private PaymentTransactionService paymentTransactionService;
	
	@Autowired	
	public PaymentTransactionController(PaymentTransactionService paymentTransactionService) {
		this.paymentTransactionService = paymentTransactionService;
	}
	
	@PostMapping(path ="/{transaction-date}/{transaction-details}/{amount}/{debit-credit}/{savings-account-id}/mutual-fund")
	public void initiatePaymentTransaction(
			@PathVariable(value="transaction-date")String transactionDate, 
			@PathVariable(value="transaction-details") String transactionDetails,  
			@PathVariable(value="amount") double amount,
			@PathVariable(value="debit-credit") String debitOrCredit,
			@PathVariable(value="savings-account-id") UUID savingsAccountId
			) {
		PaymentTransactionDTO paymentTransactionDTO = new PaymentTransactionDTO();
		paymentTransactionDTO.setTransactionDate(transactionDate);
		paymentTransactionDTO.setTransactionDetails(transactionDetails);
		paymentTransactionDTO.setAmount(amount);
		paymentTransactionDTO.setDebitOrCredit(debitOrCredit);
		paymentTransactionDTO.setSavingsAccountId(savingsAccountId);
		
	  PaymentTransaction paymentTransaction = paymentTransactionService.paymentTransaction(paymentTransactionDTO);
	
	}
	
	
	// HTTP GET with Spring MVC
    // One-To-Many Mapping (BI-Directional: From  MutualFundPortfolio To List <MutualFund> Object)
	  @GetMapping(path ="/{savings-account-id}")
	    public List<PaymentTransactionResponse > getPaymentTransactions(
	    		@PathVariable(value="savings-account-id") UUID savingsAccountId){
		  
		  List <PaymentTransaction> paymentTransactionList   = new ArrayList<>();
		  List <PaymentTransactionResponse> paymentTransactionResponseList   = new ArrayList<>();
		  
		  ModelMapper modelMapper = new ModelMapper(); 	
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			
			paymentTransactionList = paymentTransactionService.getPaymentTransactions(savingsAccountId);
			  
			 paymentTransactionResponseList = Arrays.asList( modelMapper.map(paymentTransactionList, PaymentTransactionResponse[].class));
			return  paymentTransactionResponseList;
			
	    }
}
