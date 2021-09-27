package com.codingz2m.mutualfund.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingz2m.mutualfund.data.MutualFund;
import com.codingz2m.mutualfund.services.MutualFundService;
import com.codingz2m.mutualfund.shared.MutualFundDTO;
import com.codingz2m.mutualfund.ui.models.MutualFundRequest;
import com.codingz2m.mutualfund.ui.models.MutualFundResponse;

import feign.FeignException.FeignServerException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.core.env.Environment;


@RestController
@RequestMapping("api/v1/mutual-fund")
public class MutualFundController {
	
	@Autowired
	private Environment env;
	private MutualFundService mutualFundService;
	private SavingsAccountProxy savingsAccountProxy;
	private PaymentTransactionProxy paymentTransactionProxy;
	
	@Autowired	
	public MutualFundController(@Qualifier("mutualFundService") MutualFundService mutualFundService,
			SavingsAccountProxy savingsAccountProxy,
			PaymentTransactionProxy paymentTransactionProxy) {
		super();
		this.mutualFundService = mutualFundService;
		this.savingsAccountProxy = savingsAccountProxy;
		this.paymentTransactionProxy = paymentTransactionProxy;
	}
	
	
	@GetMapping("/status/check")
	public String status() throws UnknownHostException {
		return "Trending Movie Service:  working on port - " + env.getProperty("server.port") + 
				"   Host Address: " + InetAddress.getLocalHost().getHostAddress() + 
				"   Host Name: " + InetAddress.getLocalHost().getHostName();
	}

	// HTTP POST with SpringMVC - Create new Product
	@PostMapping(
			consumes= { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			produces= { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
		)
	public ResponseEntity<MutualFundResponse> createMutualFund( 
			@Valid @RequestBody MutualFundRequest mutualFundRequest ) {
		MutualFund mutualFund=null;
		ModelMapper modelMapper=null;
		MutualFundResponse mutualFundResponse = null;
		MutualFundDTO mutualFundDTO = null;
		boolean fundTransactionState=false;
				
		 try {
		 fundTransactionState = savingsAccountProxy.initiateMutualFundTransaction(
				mutualFundRequest.getSavingsAccountId(),mutualFundRequest.getInvestedValue());
		 	modelMapper = new ModelMapper(); 	
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			mutualFundDTO = modelMapper.map( mutualFundRequest, MutualFundDTO.class); 
		 if (fundTransactionState) {									
				mutualFund  = mutualFundService.createMutualFund(mutualFundDTO);
				mutualFundResponse = modelMapper.map(mutualFund, MutualFundResponse.class);
				
				paymentTransactionProxy.initiatePaymentTransaction(mutualFundRequest.getInvestmentDate(),
						mutualFundRequest.getTransactionDetails(),
						mutualFundRequest.getInvestedValue(), 
						mutualFundRequest.getDebitOrCredit(),
						mutualFundRequest.getSavingsAccountId() );
			}
		 else {
			 
			 mutualFundResponse = new MutualFundResponse();
			 mutualFundResponse.setFundTransactionState("INSUFFICIENT FUND IN YOUR SAVINGS ACCOUNT: " 
						+ mutualFundRequest.getSavingsAccountId());
		 }
	    } catch(org.springframework.aop.AopInvocationException aie)	{
			//	mutualFundResponse.setFundTransactionState("INSUFFICIENT FUND IN YOUR SAVINGS ACCOUNT: " 
			//			+ mutualFundRequest.getSavingsAccountId());
				//return ResponseEntity.status(HttpStatus.CREATED).body(mutualFundResponse);
			}
		 catch (FeignServerException fe){
			// mutualFundResponse.setFundTransactionState("INSUFFICIENT FUND IN YOUR SAVINGS ACCOUNT: " 
			//			+ mutualFundRequest.getSavingsAccountId());
			// return ResponseEntity.status(HttpStatus.CREATED).body(mutualFundResponse);
		 }
		 catch (NullPointerException ne){
			// mutualFundResponse.setFundTransactionState("INSUFFICIENT FUND IN YOUR SAVINGS ACCOUNT: " 
				//		+ mutualFundRequest.getSavingsAccountId());
			 //return ResponseEntity.status(HttpStatus.CREATED).body(mutualFundResponse);
		 }
	
		 return ResponseEntity.status(HttpStatus.CREATED).body(mutualFundResponse);
  }
	
	

}

