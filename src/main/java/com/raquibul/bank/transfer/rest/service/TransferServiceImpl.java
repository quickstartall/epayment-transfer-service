package com.raquibul.bank.transfer.rest.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.raquibul.bank.transfer.rest.InsufficientFundException;
import com.raquibul.bank.transfer.rest.TransferRestApiException;
import com.raquibul.bank.transfer.rest.model.Account;
import com.raquibul.bank.transfer.rest.model.Transfer;
import com.raquibul.bank.transfer.rest.repository.AccountJpaRepository;
import com.raquibul.bank.transfer.rest.repository.TransferJpaRepository;
import com.raquibul.bank.transfer.rest.util.TransferRestApiUtil;

@Service
public class TransferServiceImpl implements TransferService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferServiceImpl.class);

	@Autowired
	private ConversionService conversionService;

	@Autowired
	private TransferJpaRepository transferJpaRepository;

	@Autowired
	private AccountJpaRepository accountJpaRepository;
	/**
	 * 
	 */
	@Override
	@Transactional
	public void transfer(Transfer transfer) throws InsufficientFundException, TransferRestApiException {
		//get from and to Account from the transfer
		Account fromAccount = this.conversionService.convert(this.accountJpaRepository.findByName(transfer.getFromAccount()), Account.class);
		Account toAccount = this.conversionService.convert(this.accountJpaRepository.findByName(transfer.getToAccount()), Account.class);
		
		//is the from account valid
		if (fromAccount == null) {
			throw new TransferRestApiException("from Account not found for the account name=" + transfer.getFromAccount());
		}
		//is the to account valid
		if (toAccount == null) {
			throw new TransferRestApiException("to Account not found for the account name=" + transfer.getToAccount());
		}
		//if there is sufficient fund to transfer the amount
		if (!TransferRestApiUtil.isSufficientBalance(fromAccount, transfer.getAmount())) {
			throw new InsufficientFundException("Not sufficient fund to transfer the amount");
		}
		transact(fromAccount, toAccount, transfer.getAmount());
	}

	private void transact(Account from, Account to, BigDecimal amount) {
		synchronized (Account.class) {
			BigDecimal fromBalanceAfter = from.getBalance().subtract(amount);
			BigDecimal toBalanceAfter = to.getBalance().add(amount);
			from.setBalance(fromBalanceAfter);
			to.setBalance(toBalanceAfter);
			this.accountJpaRepository.save(this.conversionService.convert(from, com.raquibul.bank.transfer.rest.jpa.entity.Account.class));
			this.accountJpaRepository.save(this.conversionService.convert(to, com.raquibul.bank.transfer.rest.jpa.entity.Account.class));
		}
	}
	
	/**
	 * 
	 */
	@Transactional
	@Override
	public Transfer addTransfer(Transfer transfer) throws TransferRestApiException, InsufficientFundException {
		try {
			transfer.setTransferDate(new Date()); //add time to current system time
			
			//transfer the amount
			transfer(transfer);
			
			//save transsfer request for audit
			com.raquibul.bank.transfer.rest.jpa.entity.Transfer transferJpa = this.conversionService.convert(transfer,
					com.raquibul.bank.transfer.rest.jpa.entity.Transfer.class);
			return this.conversionService.convert(this.transferJpaRepository.save(transferJpa),
					com.raquibul.bank.transfer.rest.model.Transfer.class);
		} catch (DataAccessException e) {
			LOGGER.error("addTransfer :: There was an error adding account to the repository", e);
			throw new TransferRestApiException(e);
		} catch (Exception e) {
			LOGGER.error("addTransfer :: There was an unknown exception while performing add", e);
			throw new TransferRestApiException(e);
		}
	}

	@Override
	@Transactional
	public List<Transfer> getTransactionsBetween(Date startDate, Date endDate) 
			throws TransferRestApiException {
		try {
			final List<Transfer> result = new ArrayList<>();
			List<com.raquibul.bank.transfer.rest.jpa.entity.Transfer> transactions = 
					this.transferJpaRepository.findByTransferDateBetween(startDate, endDate);
			for (com.raquibul.bank.transfer.rest.jpa.entity.Transfer transfer : transactions) {
				result.add(this.conversionService.convert(transfer, Transfer.class));
			}
			return result;
		} catch (DataAccessException e) {
			LOGGER.error("getTransactionsBetween :: There was an error fetching accounts from the repository", e);
			throw new TransferRestApiException(e);
		} catch (Exception e) {
			LOGGER.error("getTransactionsBetween :: There was an unknown exception", e);
			throw new TransferRestApiException(e);
		} 
	}

	@Override
	@Transactional
	public List<Transfer> getAllTransactions() throws TransferRestApiException {
		try {
			final List<Transfer> result = new ArrayList<>();
			for (com.raquibul.bank.transfer.rest.jpa.entity.Transfer transfer : this.transferJpaRepository.findAll()) {
				result.add(this.conversionService.convert(transfer, Transfer.class));
			}
			return result;
		} catch (DataAccessException e) {
			LOGGER.error("getTransactionsBetween :: There was an error fetching accounts from the repository", e);
			throw new TransferRestApiException(e);
		} catch (Exception e) {
			LOGGER.error("getTransactionsBetween :: There was an unknown exception", e);
			throw new TransferRestApiException(e);
		}
	}
	
}
