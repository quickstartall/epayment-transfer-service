package com.raquibul.bank.transfer.rest;

/**
 * Exception class for any checked exception in the module
 * @see Exception
 * @author Raquibul Hasan
 *
 */
@SuppressWarnings("serial")
public class TransferRestApiException extends Exception{
	public TransferRestApiException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TransferRestApiException(String message) {
		super(message);
	}
	
	public TransferRestApiException(Throwable cause) {
		super(cause);
	}
}
