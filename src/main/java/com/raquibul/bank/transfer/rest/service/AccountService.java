package com.raquibul.bank.transfer.rest.service;

import java.util.List;

import com.raquibul.bank.transfer.rest.TransferRestApiException;
import com.raquibul.bank.transfer.rest.model.Account;

/**
 * 
 * @author Raquibul Hasan
 *
 */
public interface AccountService {
	/**
	 *
	 * @return
	 * @throws TransferRestApiException
	 */
	Account addAccount(Account account) throws TransferRestApiException;
	/**
	 * 
	 * @return
	 * @throws TransferRestApiException
	 */
	List<Account> getAllAccounts() throws TransferRestApiException;
	/**
	 *
	 * @return
	 * @throws TransferRestApiException
	 */
	Account getAccount(long id) throws TransferRestApiException;
	/**
	 *
	 * @throws TransferRestApiException
	 */
	void updateAccount(Account account) throws TransferRestApiException;
	/**
	 *
	 * @throws TransferRestApiException
	 */
	void deleteAccount(long id) throws TransferRestApiException;
	boolean doesAccountExist(long id) throws TransferRestApiException;
}
