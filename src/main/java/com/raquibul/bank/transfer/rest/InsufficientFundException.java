package com.raquibul.bank.transfer.rest;

@SuppressWarnings("serial")
public class InsufficientFundException extends Exception{
	public InsufficientFundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InsufficientFundException(String message) {
		super(message);
	}
	
	public InsufficientFundException(Throwable cause) {
		super(cause);
	}
}
