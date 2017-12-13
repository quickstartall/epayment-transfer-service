package com.raquibul.bank.transfer.rest;

@SuppressWarnings("serial")
public class RequestValidationException extends RuntimeException {
	public RequestValidationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RequestValidationException(String message) {
		super(message);
	}
	
	public RequestValidationException(Throwable cause) {
		super(cause);
	}
}
