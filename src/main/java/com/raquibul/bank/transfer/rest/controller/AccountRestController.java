package com.raquibul.bank.transfer.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.raquibul.bank.transfer.rest.ApiError;
import com.raquibul.bank.transfer.rest.TransferRestApiException;
import com.raquibul.bank.transfer.rest.TransferRestExceptionHandler;
import com.raquibul.bank.transfer.rest.model.Account;
import com.raquibul.bank.transfer.rest.service.AccountService;
import com.raquibul.bank.transfer.rest.service.AccountServiceImpl;
import com.raquibul.bank.transfer.rest.util.TransferRestApiUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/** The Controller for to cater the Account related request
 * @see AbstractRestController
 * @see TransferRestExceptionHandler
 * @see ApiError
 * @see TransferRestApiException
 * @see AccountServiceImpl
 * @author Raquibul Hasan 
 *
 */
@RestController
public class AccountRestController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountRestController.class);
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * GET operation to get Account by Id
	 * @param id - the Account Id
	 * @return {@link ResponseEntity} - the entity with the object (API error or actual result)
	 * @throws TransferRestApiException
	 */
	@CrossOrigin
	@RequestMapping (
		value = "${payment.rest.api.account.get.uri}/{id}",
		method = RequestMethod.GET,
		produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }
	)
	@ApiOperation(value = "View an account by it's Id", response = Iterable.class)
	@ApiResponses(
		value = {
		        @ApiResponse (code = 200, message = "Successfully retrieved account list"),
		        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<?> getAccount(@PathVariable Long id) throws TransferRestApiException {
		ApiError error = null;
		Account account = null;
		LOGGER.debug("getAccount :: controller invoked, Account ID={}", id);
		
		try {
			if ( id == null ||  id <= 0) {
				error = new ApiError(HttpStatus.BAD_REQUEST, "Entered ID is 0 or negative", "Entered ID is 0 or negative");
				LOGGER.error("getAccount :: provided ID 0 or negative");
				return  createResponse(null, error, HttpStatus.BAD_REQUEST);
			} 
			account = accountService.getAccount(id);
		} catch (TransferRestApiException e) {
			LOGGER.error("getAccount :: There was an error getting Account", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("getAccount :: There was an exception in the Account service", e);
			throw e;
		}
		return createResponse(account, error, HttpStatus.OK);
	}
	
	/**
	 * GET operation to return all the accounts
	 * @return {@link ResponseEntity} - the entity with the object (API error or actual result)
	 * @throws TransferRestApiException
	 */
	@CrossOrigin
	@RequestMapping (
		value = "${payment.rest.api.accounts.uri}",
		method = RequestMethod.GET,
		produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }
	)
	@ApiOperation(value = "View a list of available accounts", response = Iterable.class)
	@ApiResponses(
		value = {
		        @ApiResponse (code = 200, message = "Successfully retrieved account list"),
		        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<?> getAllAccounts() throws TransferRestApiException {
		ApiError error = null;
		List<Account> resultList = new ArrayList<>();
		LOGGER.debug("getAllAccounts :: controller invoked");
		try {
			resultList = accountService.getAllAccounts();
		} catch (Exception e) {
			LOGGER.error("getAllAccounts :: There was an exception in the Account service", e);
			throw e;
		}
		return createResponse(resultList, error, HttpStatus.OK);
	}
	/**
	 * POST operation to add An Account
	 * @param account - the account to add
	 * @return {@link ResponseEntity} - the entity with the object (API error or actual result)
	 * @throws TransferRestApiException - in case of application level exception
	 */
	@CrossOrigin
	@RequestMapping (
		value = "${payment.rest.api.account.add.uri}",
		method = RequestMethod.POST,
		produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }
	)
	@ApiOperation(value = "add an account", response = Account.class)
	@ApiResponses(
		value = {
		        @ApiResponse (code = 201, message = "Successfully created/added account"),
		        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<?> addAccount(@RequestBody Account account) throws TransferRestApiException {
		ApiError error = null;
		Account accountModel = null;
		LOGGER.debug("addAccount :: controller invoked account={}", account);
		try {
			if ( account == null || TransferRestApiUtil.isNegativeBalance(account.getBalance()) ) {
				error = new ApiError(HttpStatus.BAD_REQUEST, "Provided account is invalid", "Provided account is invalid");
				LOGGER.error("addAccount :: provided account is invalid, account={}", account);
				return createResponse(null, error, HttpStatus.BAD_REQUEST);
			} 
			accountModel = accountService.addAccount(account);
		} catch (TransferRestApiException e) {
			LOGGER.error("addAccount :: There was an error adding Account", e);
			throw e;
		} catch ( Exception e) {
			LOGGER.error("addAccount :: There was an exception in the account service", e);
			throw e;
		}
		return createResponse(accountModel, error, HttpStatus.CREATED);
	}
	
	/**
	 * PUT operation to update the account
	 * @return {@link ResponseEntity} - the entity with the object (API error or actual result)
	 * @throws TransferRestApiException 
	 */
	@CrossOrigin
	@RequestMapping (
		value = "${payment.rest.api.account.update.uri}",
		method = RequestMethod.PUT,
		produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }
	)
	@ApiOperation(value = "Update an accounts", response = Account.class)
	@ApiResponses(
		value = {
		        @ApiResponse (code = 201, message = "Successfully updates the account"),
		        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<?> updateAccount(@RequestBody Account account) throws TransferRestApiException {
		ApiError error = null;
		LOGGER.debug("updateAccount :: controller invoked, Account={}", account);
		
		try {
			if ( account == null || TransferRestApiUtil.isNegativeBalance(account.getBalance())) {
				error = new ApiError(HttpStatus.BAD_REQUEST, "Provided Account is invalid", "Provided Account is invalid");
				LOGGER.error("updateAccount :: provided Account is invalid, account={}", account);
				return createResponse(null, error, HttpStatus.BAD_REQUEST);
			} 
			this.accountService.updateAccount(account);
		} catch ( TransferRestApiException e) {
			LOGGER.error("updateAccount :: There was an error updating Account", e);
			throw e;
		}catch ( Exception e) {
			LOGGER.error("updateAccount :: There was an exception in the Account service", e);
			throw e;
		}
		return createResponse(account, error, HttpStatus.CREATED);
	}
	
	/**
	 *	DELETE operation to delete an Account by Id
	 *	@param id - the account id
	 * @return {@link ResponseEntity} - the entity with the object (API error or actual result)
	 * @throws TransferRestApiException - in case of any appkication exception
	 */
	@CrossOrigin
	@RequestMapping (
		value = "${payment.rest.api.account.delete.uri}/{id}",
		method = RequestMethod.DELETE,
		produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }
	)
	@ApiOperation(value = "Delete an account", response = Iterable.class)
	@ApiResponses(
		value = {
		        @ApiResponse (code = 204, message = "Successfully deleted account"),
		        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<?> deleteAccount(@PathVariable Long id) throws TransferRestApiException {
		ApiError error = null;
		LOGGER.debug("deleteAccount :: controller invoked, account.ID={}", id);
		try {
			if ( id == null ||  id <= 0) {
				error = new ApiError(HttpStatus.BAD_REQUEST, "Entered ID is invalid", "Entered ID invalid");
				LOGGER.error("deleteAccount :: provided ID is invalid, ID={}", id);
				return createResponse(null, error, HttpStatus.BAD_REQUEST);
			} 
			this.accountService.deleteAccount(id);
		} catch (TransferRestApiException e) {
			LOGGER.error("deleteAccount :: There was an error deleting Account", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("deleteAccount :: There was an exception in the Account service", e);
			throw e;
		}
		return createResponse(null, error, HttpStatus.NO_CONTENT);
	}
}
