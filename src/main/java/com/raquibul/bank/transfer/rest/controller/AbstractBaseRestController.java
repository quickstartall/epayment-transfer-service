package com.raquibul.bank.transfer.rest.controller;

import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import com.raquibul.bank.transfer.rest.ApiError;
import com.raquibul.bank.transfer.rest.RequestValidationException;
import com.raquibul.bank.transfer.rest.model.Account;
import com.raquibul.bank.transfer.rest.util.TransferRestApiUtil;

import io.swagger.annotations.Api;

/**
 * The Abstract base class to be sub-classed in all the Controllers. It defines
 * the base URI and common functionalities needed in all the Controllers
 * 
 * @author Raquibul Hasan
 *
 */
@RequestMapping("${payment.rest.api.base.uri}")
@Api(value = "payment", description = "Transfer Service REST API")
public abstract class AbstractBaseRestController {
	protected abstract Logger getLogger();
//	public static ResponseEntity<?> createResponse(Object obj, ApiError error, HttpStatus status) {
//		if (error != null) {
//			return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
//		} else {
//			return obj != null ? new ResponseEntity<Object>(obj, status) : new ResponseEntity<Object>(status);
//		}
//	}
	
	public static ResponseEntity<?> createResponse(ApiError error) {
		return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
	}
	
	public static ResponseEntity<?> createResponse(Object obj, HttpStatus status) {
		return new ResponseEntity<Object>(obj, status);
	}
	
	public static ResponseEntity<?> createResponse(HttpStatus status) {
		return new ResponseEntity<Object>(status);
	}
	
	public static ResponseEntity<?> createResponse(HttpHeaders httpheader, HttpStatus status) {
		return new ResponseEntity<Object>(httpheader, status);
	}
	
	protected void validateRequestForNegativeValue(String paramName, Long paramValue) {
		if (paramValue == null || paramValue <= 0) {
			StringBuilder errorMessage = new StringBuilder("The provided parameter [");
			errorMessage.append(paramName).append("] is zero or negative");
			getLogger().debug("validateRequestForNegativeValue :: request param value [paramValue={}]", paramValue);
			throw new RequestValidationException(errorMessage.toString());
		}
			
	}
	
	protected void validateRequestForNullValue(String paramName, Object paramValue) {
		if (paramValue == null) {
			StringBuilder errorMessage = new StringBuilder("The provided parameter [");
			errorMessage.append(paramName).append("] is null");
			getLogger().debug("validateRequestForNullValue :: request param value [paramValue={}]", paramValue);
			throw new RequestValidationException(errorMessage.toString());
		}
	}
	
	protected void validateRequestForNegativeBalance(Account account) {
		if (TransferRestApiUtil.isNegativeBalance(account.getBalance())) {
			getLogger().debug("validateRequestForNegativeBalance :: negative balance [balance={}]", account.getBalance());
			throw new RequestValidationException("Balance can not be negative");
		}
	}
	
}
