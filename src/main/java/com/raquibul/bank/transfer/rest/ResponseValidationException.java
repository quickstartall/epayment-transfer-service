package com.raquibul.bank.transfer.rest;

@SuppressWarnings("serial")
public class ResponseValidationException extends RuntimeException {
	public ResponseValidationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ResponseValidationException(String message) {
		super(message);
	}
	
	public ResponseValidationException(Throwable cause) {
		super(cause);
	}

}
