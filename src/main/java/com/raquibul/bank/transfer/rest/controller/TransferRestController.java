package com.raquibul.bank.transfer.rest.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raquibul.bank.transfer.rest.RequestValidationException;
import com.raquibul.bank.transfer.rest.TransferRestApiException;
import com.raquibul.bank.transfer.rest.TransferRestExceptionHandler;
import com.raquibul.bank.transfer.rest.model.Account;
import com.raquibul.bank.transfer.rest.model.Transfer;
import com.raquibul.bank.transfer.rest.service.TransferService;
import com.raquibul.bank.transfer.rest.service.TransferServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * The Rest Controller to cater transfer requests
 * @see AbstractBaseRestController
 * @see TransferRestExceptionHandler
 * @see TransferRestApiException
 * @see RequestValidationException
 * @see TransferServiceImpl
 * @author Raquibul Hasan
 *
 */
@RestController
public class TransferRestController extends AbstractBaseRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountRestController.class);

	@Autowired
	private TransferService transferService;
	
	/**
	 * GET Operation to return all the Transactions performed 
	 * @return {@link ResponseEntity} - the entity with the object (API error or actual result)
	 * @throws Exception - in case of any Exception in the application
	 */
	@CrossOrigin
	@RequestMapping (
		value = "${payment.rest.api.transactions.uri}",
		method = RequestMethod.GET,
		produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }
	)
	@ApiOperation(value = "View a list of Transactions/Transfers", response = Iterable.class)
	@ApiResponses(
		value = {
		        @ApiResponse (code = 200, message = "Successfully retrieved transaction/transfer list"),
		        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<?> getAllTransactions() throws Exception {
		List<Transfer> transactions = null;
		LOGGER.debug("getAllTransactions :: controller invoked");
		try {
			transactions = transferService.getAllTransactions();
		} catch (TransferRestApiException e) {
			LOGGER.error("getAllTransactions :: There was an error getting Account", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("getAllTransactions :: There was an exception in the Account service", e);
			throw e;
		}
		return createResponse(transactions, HttpStatus.OK);
	}
	
	
	/**
	 * GET operation to return the Transactions between startDate and endDate
	 * @param startDate - the start date
	 * @param endDate - the end date
	 * @return {@link ResponseEntity} - the entity with the object (API error or actual result)
	 * @throws Exception 
	 */
	@CrossOrigin
	@RequestMapping (
		value = "${payment.rest.api.transactions.between.uri}",
		method = RequestMethod.GET,
		produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }
	)
	@ApiOperation(value = "View a list of available Transaction/Transfers filtered by dates", response = Iterable.class)
	@ApiResponses(
		value = {
		        @ApiResponse (code = 200, message = "Successfully retrieved Transactions/Transfers list"),
		        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<?> getTransactions(@RequestParam  @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) throws Exception {
		List<Transfer> transactions = null;
		LOGGER.debug("getTransactions :: controller invoked, startDate={}, endDate={}", startDate, endDate );
		try {
			if (startDate.after(endDate)) {
				LOGGER.error("getTransactions :: provided ID 0 or negative");
				throw new RequestValidationException("Start Date can not be greater than End Date");
			}
			transactions = transferService.getTransactionsBetween(startDate, endDate);
		} catch (TransferRestApiException e) {
			LOGGER.error("getTransactions :: There was an error getting Account", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("getTransactions :: There was an exception in the Account service", e);
			throw e;
		}
		return createResponse(transactions, HttpStatus.OK);
	}
	
	/**
	 * The POST operation to add/initiate a transaction/transfer
	 * @param transfer - the request object with the transfer details
	 * @return {@link ResponseEntity} - the entity with the object (API error or actual result)
	 * @throws Exception - in case of any exception
	 */
	@CrossOrigin
	@RequestMapping (
		value = "${payment.rest.api.transaction.uri.add}",
		method = RequestMethod.POST,
		produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }
	)
	@ApiOperation(value = "add a Transfer", response = Account.class)
	@ApiResponses(
		value = {
		        @ApiResponse (code = 201, message = "Successfully Completed and Added Transfer"),
		        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public ResponseEntity<?> addTransfer(@RequestBody (required=false) Transfer transfer) throws Exception {
		Transfer transferModel = null;
		LOGGER.debug("addAccount :: controller invoked transfer={}", transfer);
		
		validateRequestForNullValue("transfer", transfer);
		try {
			if (StringUtils.isEmpty(transfer.getFromAccount())) {
				LOGGER.error("addAccount :: invalid fromAccount, fromAccount={}", transfer.getFromAccount());
				throw new RequestValidationException("invalid fromAccount");
			}
			
			if (StringUtils.isEmpty(transfer.getToAccount())) {
				LOGGER.error("addAccount :: invalid toAccount, toAccount={}", transfer.getToAccount());
				throw new RequestValidationException("invalid toAccount");
			}
			transferModel = transferService.addTransfer(transfer);
		} catch (TransferRestApiException e) {
			LOGGER.error("addAccount :: There was an error adding Account", e);
			throw e;
		} catch ( Exception e) {
			LOGGER.error("addAccount :: There was an exception in the account service", e);
			throw e;
		}
		return createResponse(transferModel, HttpStatus.CREATED);
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
