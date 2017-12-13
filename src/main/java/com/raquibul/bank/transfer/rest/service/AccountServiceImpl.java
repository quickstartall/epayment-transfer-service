package com.raquibul.bank.transfer.rest.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.raquibul.bank.transfer.rest.TransferRestApiException;
import com.raquibul.bank.transfer.rest.model.Account;
import com.raquibul.bank.transfer.rest.repository.AccountJpaRepository;

/**
 * Service for the Account related functionalities. It implements basic CRUD functionality
 * @author Raquibul Hasan 
 *
 */
@Service
public class AccountServiceImpl implements AccountService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	@Autowired
	private ConversionService conversionService;
	@Autowired
	private AccountJpaRepository accountJpaRepository;
	
	/**
	 * 
	 */
	@Override
	@Transactional
	public List<Account> getAllAccounts() 
			throws TransferRestApiException {
		try {
			final List<Account> result = new ArrayList<>();
			for (com.raquibul.bank.transfer.rest.jpa.entity.Account account : this.accountJpaRepository.findAll()) {
				result.add(this.conversionService.convert(account, Account.class));
			}
			return result;
		} catch (DataAccessException e) {
			LOGGER.error("getAllAccounts :: There was an error fetching accounts from the repository", e);
			throw new TransferRestApiException(e);
		} catch (Exception e) {
			LOGGER.error("getAllAccounts :: There was an unknown exception", e);
			throw new TransferRestApiException(e);
		} 
	}
	/**
	 * Get Account by Id
	 * @param id - the Account Id
	 * @return Account - the Account by Id
	 */
	@Override
	public Account getAccount(long id) 
			throws TransferRestApiException {
		try {
			return this.conversionService.convert(this.accountJpaRepository.findOne(id), 
					Account.class);
		} catch (DataAccessException e) {
			LOGGER.error("getAllAccounts :: There was an error fetching account from the repository", e);
			throw new TransferRestApiException(e);
		} catch (Exception e) {
			LOGGER.error("getAccount :: There was an unknown exception", e);
			throw new TransferRestApiException(e);
		}
	}
	
	/**
	 * Get Account by Id
	 * @param id - the Account Id
	 * @return Account - the Account by Id
	 */
	@Override
	public boolean doesAccountExist(long id) 
			throws TransferRestApiException {
		try {
			return this.accountJpaRepository.exists(id);
		} catch (Exception e) {
			LOGGER.error("getAccount :: There was an unknown exception", e);
			throw new TransferRestApiException(e);
		}
	}
	
	/**
	 * Add Account
	 */
	@Override
	public Account addAccount(
			Account account) 
		throws TransferRestApiException {
		try {
			com.raquibul.bank.transfer.rest.jpa.entity.Account accountJpa 
				= this.conversionService.convert(account, com.raquibul.bank.transfer.rest.jpa.entity.Account.class);
			return this.conversionService.convert(this.accountJpaRepository.save(accountJpa), 
					com.raquibul.bank.transfer.rest.model.Account.class);
		} catch (DataAccessException e) {
			LOGGER.error("addAccount :: There was an error adding account to the repository", e);
			throw new TransferRestApiException(e);
		} catch (Exception e) {
			LOGGER.error("addAccount :: There was an unknown exception while performing add", e);
			throw new TransferRestApiException(e);
		}
	}	
	/**
	 * Update Account
	 */
	@Override
	@Transactional
	public void updateAccount(Account account) 
		throws TransferRestApiException {
		try {
			this.accountJpaRepository.save(this.conversionService.convert(account, 
					com.raquibul.bank.transfer.rest.jpa.entity.Account.class));
		} catch (DataAccessException e) {
			LOGGER.error("updateAccount :: There was an error updating account to the repository", e);
			throw new TransferRestApiException(e);
		} catch (Exception e) {
			LOGGER.error("There was an unknown exception", e);
			throw new TransferRestApiException(e);
		}
	}
	/**
	 * Delete Account by ID
	 */
	@Override
	@Transactional
	public void deleteAccount(long id) throws TransferRestApiException {
		try {
			this.accountJpaRepository.delete(id);
		} catch (DataAccessException e) {
			LOGGER.error("deleteAccount :: There was an error deleting account from the repository", e);
			throw new TransferRestApiException(e);
		} catch (Exception e) {
			LOGGER.error("deleteAccount :: There was an unknown exception", e);
			throw new TransferRestApiException(e);
		} 
	}
}
