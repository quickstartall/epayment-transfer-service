package com.raquibul.bank.transfer.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * Error class to map Exceptions to application level JSOM error message as Response
 * @author Raquibul Hasan
 *
 */
public class ApiError {
	private final HttpStatus status;
	private final String message;
	private final List<String> errors;
	
	/**
	 * @return the status
	 */
	public HttpStatus getStatus() {
		return status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	public ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
 
    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
    
    public ApiError(String message, String error) {
        super();
        this.status = null;
        this.message = message;
        errors = Arrays.asList(error);
    }

}
