package com.raquibul.bank.transfer.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.raquibul.bank.transfer.rest.RequestValidationException;
import com.raquibul.bank.transfer.rest.ResponseValidationException;
import com.raquibul.bank.transfer.rest.TransferRestApiException;
import com.raquibul.bank.transfer.rest.TransferRestExceptionHandler;
import com.raquibul.bank.transfer.rest.model.Account;
import com.raquibul.bank.transfer.rest.service.AccountService;
import com.raquibul.bank.transfer.rest.service.AccountServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/** The Controller for to cater the Account related request
 * @see AbstractBaseRestController
 * @see TransferRestExceptionHandler
 * @see TransferRestApiException
 * @see RequestValidationException
 * @see ResponseValidationException
 * @see AccountServiceImpl
 * @author Raquibul Hasan 
 *
 */
@RestController
public class AccountRestController extends AbstractBaseRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountRestController.class);
	
	@Autowired
	private AccountService accountService;
	
	@Value (value = "${payment.rest.api.base.uri}/${payment.rest.api.account.get.uri}")
	private String accountGetUri;
	
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
		Account account = null;
		LOGGER.debug("getAccount :: controller invoked, Account ID={}", id);
		
		try {
			//validate path param
			validateRequestForNegativeValue("id", id);
			account = accountService.getAccount(id);
		} catch (TransferRestApiException e) {
			LOGGER.error("getAccount :: There was an error getting Account", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("getAccount :: There was an exception in the Account service", e);
			throw e;
		}
		return createResponse(account, HttpStatus.OK);
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
		List<Account> resultList = new ArrayList<>();
		LOGGER.debug("getAllAccounts :: controller invoked");
		try {
			resultList = accountService.getAllAccounts();
		} catch (Exception e) {
			LOGGER.error("getAllAccounts :: There was an exception in the Account service", e);
			throw e;
		}
		return createResponse(resultList, HttpStatus.OK);
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
	public ResponseEntity<?> createAccount(@RequestBody Account account) throws TransferRestApiException {
		Account accountModel = null;
		LOGGER.debug("addAccount :: controller invoked account={}", account);
		HttpHeaders headers = null;
		try {
			validateRequestForNullValue("account", account);
			validateRequestForNegativeBalance(account);
			
			if (account.getId() != null && this.accountService.doesAccountExist(account.getId())) {
				LOGGER.debug("addAccount :: Account with provided ID already exist. It can not be created");
				throw new ResponseValidationException("Account can not be created. Account with the id already exists");
			}
			
			accountModel = accountService.addAccount(account);

			headers = new HttpHeaders();
			headers.setLocation(UriComponentsBuilder.fromPath(accountGetUri + "/{id}").buildAndExpand(accountModel.getId()).toUri());
		} catch (TransferRestApiException e) {
			LOGGER.error("addAccount :: There was an error adding Account", e);
			throw e;
		} catch ( Exception e) {
			LOGGER.error("addAccount :: There was an exception in the account service", e);
			throw e;
		}
		return createResponse(headers, HttpStatus.CREATED);
	}
	
	/**
	 * PUT operation to update the account
	 * @return {@link ResponseEntity} - the entity with the object (API error or actual result)
	 * @throws TransferRestApiException 
	 */
	@CrossOrigin
	@RequestMapping (
		value = "${payment.rest.api.account.update.uri}/{id}",
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
	public ResponseEntity<?> updateAccount(@PathVariable Long id,  @RequestBody Account account) throws TransferRestApiException {
		LOGGER.debug("updateAccount :: controller invoked, Account={}", account);
		try {
			validateRequestForNullValue("account", account);
			validateRequestForNegativeBalance(account);
			
			if (!this.accountService.doesAccountExist(id)) {
				LOGGER.debug("updateAccount :: The account does not exist in Database [account.id={}]", id);
				throw new ResponseValidationException("The account can not be updated. It does not exist in the database");
			}
			this.accountService.updateAccount(account);
		} catch ( TransferRestApiException e) {
			LOGGER.error("updateAccount :: There was an error updating Account", e);
			throw e;
		}catch ( Exception e) {
			LOGGER.error("updateAccount :: There was an exception in the Account service", e);
			throw e;
		}
		return createResponse(HttpStatus.NO_CONTENT);
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
		LOGGER.debug("deleteAccount :: controller invoked, account.ID={}", id);
		validateRequestForNegativeValue("id", id);
		try {
			if (!this.accountService.doesAccountExist(id)) {
				LOGGER.debug("deleteAccount :: The account to be deleted does not exist in Database");
				throw new ResponseValidationException("The account to be deleted does not exist in Database");
			}
			this.accountService.deleteAccount(id);
		} catch (TransferRestApiException e) {
			LOGGER.error("deleteAccount :: There was an error deleting Account", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("deleteAccount :: There was an exception in the Account service", e);
			throw e;
		}
		return createResponse(HttpStatus.NO_CONTENT);
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
