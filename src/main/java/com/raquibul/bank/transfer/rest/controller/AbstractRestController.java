package com.raquibul.bank.transfer.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import com.raquibul.bank.transfer.rest.ApiError;

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
public abstract class AbstractRestController {
	public static ResponseEntity<?> createResponse(Object obj, ApiError error, HttpStatus status) {
		if (error != null) {
			return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
		} else {
			return obj != null ? new ResponseEntity<Object>(obj, status) : new ResponseEntity<Object>(status);
		}
	}
}
