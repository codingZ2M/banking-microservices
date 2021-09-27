package com.codingz2m.mutualfund.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingz2m.mutualfund.data.MutualFund;
import com.codingz2m.mutualfund.data.MutualFundPortfolio;
import com.codingz2m.mutualfund.services.MutualFundService;
import com.codingz2m.mutualfund.shared.MutualFundPortfolioDTO;
import com.codingz2m.mutualfund.ui.models.MutualFundPortfolioRequest;
import com.codingz2m.mutualfund.ui.models.MutualFundPortfolioResponse;
import com.codingz2m.mutualfund.ui.models.MutualFundResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("api/v1/mutual-fund-portfolio")
public class MutualFundPortfolioController {
	
	@Autowired
	private Environment env;
	private MutualFundService mutualFundService;
	
	@Autowired	
	public MutualFundPortfolioController(@Qualifier("mutualFundPortfolio") MutualFundService mutualFundService) {
		super();
		this.mutualFundService = mutualFundService;
	}

	@GetMapping("/status/check")
	public String status() throws UnknownHostException {
		return "Trending Movie Service:  working on port - " + env.getProperty("server.port") + 
				"   Host Address: " + InetAddress.getLocalHost().getHostAddress() + 
				"   Host Name: " + InetAddress.getLocalHost().getHostName();
	}
	
	// HTTP POST with SpringMVC
	@PostMapping	(consumes= { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
		)

		public ResponseEntity<MutualFundPortfolioResponse> createMutualFundPortfolio(@Valid @RequestBody MutualFundPortfolioRequest mutualFundPortfolioRequest) {
		
			ModelMapper modelMapper = new ModelMapper(); 	
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

			MutualFundPortfolioDTO  mutualFundPortfolioDTO = modelMapper.map( mutualFundPortfolioRequest, MutualFundPortfolioDTO.class); 		
		    MutualFundPortfolio mutualFundPortfolio = mutualFundService.createMutualFundPortfolio(mutualFundPortfolioDTO);

		    MutualFundPortfolioResponse mutualFundPortfolioResponse = modelMapper.map(mutualFundPortfolio, MutualFundPortfolioResponse.class);		    
			return ResponseEntity.status(HttpStatus.CREATED).body(mutualFundPortfolioResponse);

	   }
	

	// HTTP GET with Spring MVC
    // One-To-Many Mapping (BI-Directional: From  MutualFundPortfolio To List <MutualFund> Object)
	  @GetMapping(path ="/{mutual-fund-portfolio-id}/mutual-funds")
	    public ResponseEntity<List<MutualFundResponse >> getMutualFundsOfPortfolio(
	    		@PathVariable(value="mutual-fund-portfolio-id") UUID mutualFundPortfolioId){
		
		  List <MutualFundResponse> mutualFundResponse   = new ArrayList<>();
		  
		  ModelMapper modelMapper = new ModelMapper(); 	
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			
			 List<MutualFund> mutualFundList = mutualFundService.getMutualFundsOfPortfolio(mutualFundPortfolioId);
			  
			mutualFundResponse = Arrays.asList( modelMapper.map(mutualFundList, MutualFundResponse[].class));
			return ResponseEntity.status(HttpStatus.FOUND).body(mutualFundResponse );
			
	    }

	
}

