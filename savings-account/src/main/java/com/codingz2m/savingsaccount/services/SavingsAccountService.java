package com.codingz2m.savingsaccount.services;

import java.util.UUID;

import com.codingz2m.savingsaccount.data.SavingsAccount;
import com.codingz2m.savingsaccount.shared.SavingsAccountDTO;
import com.codingz2m.savingsaccount.ui.models.SavingsAccountTransactionRequest;

public interface SavingsAccountService {
	
	SavingsAccount createSavingsAccount(SavingsAccountDTO savingsAccountDTO);
	
	int savingsAccountTransaction(SavingsAccountTransactionRequest savingsAccountTransactionRequest);
	
	boolean mutualFundTransaction(UUID savingsAccountId, double mutualFundAmonunt);
	

}
