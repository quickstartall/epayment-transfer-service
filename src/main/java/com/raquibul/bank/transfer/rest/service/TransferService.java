package com.raquibul.bank.transfer.rest.service;

import java.util.Date;
import java.util.List;

import com.raquibul.bank.transfer.rest.InsufficientFundException;
import com.raquibul.bank.transfer.rest.TransferRestApiException;
import com.raquibul.bank.transfer.rest.model.Transfer;

public interface TransferService {
	public void transfer(Transfer transfer) throws InsufficientFundException, TransferRestApiException;

	Transfer addTransfer(Transfer transfer) throws TransferRestApiException, InsufficientFundException;

	List<Transfer> getAllTransactions() throws TransferRestApiException;

	List<Transfer> getTransactionsBetween(Date startDate, Date endDate) throws TransferRestApiException;
}
