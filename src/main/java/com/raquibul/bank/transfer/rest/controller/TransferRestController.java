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

import com.raquibul.bank.transfer.rest.ApiError;
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
 * @see AbstractRestController
 * @see TransferRestExceptionHandler
 * @see ApiError
 * @see TransferRestApiException
 * @see TransferServiceImpl
 * @author Raquibul Hasan
 *
 */
@RestController
public class TransferRestController extends AbstractRestController {
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
		ApiError error = null;
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
		return createResponse(transactions, error, HttpStatus.OK);
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
		ApiError error = null;
		List<Transfer> transactions = null;
		LOGGER.debug("getTransactions :: controller invoked, startDate={}, endDate={}", startDate, endDate );
		try {
			if (startDate.after(endDate)) {
				error = new ApiError(HttpStatus.BAD_REQUEST, "Start Date can not be greater than End Date", "Start Date can not be greater than End Date");
				LOGGER.error("getTransactions :: provided ID 0 or negative");
				return  createResponse(null, error, HttpStatus.BAD_REQUEST);
			}
			transactions = transferService.getTransactionsBetween(startDate, endDate);
		} catch (TransferRestApiException e) {
			LOGGER.error("getTransactions :: There was an error getting Account", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("getTransactions :: There was an exception in the Account service", e);
			throw e;
		}
		return createResponse(transactions, error, HttpStatus.OK);
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
		ApiError error = null;
		Transfer transferModel = null;
		LOGGER.debug("addAccount :: controller invoked transfer={}", transfer);
		try {
			if ( transfer == null ) {
				error = new ApiError(HttpStatus.BAD_REQUEST, "Provided transfer is invalid", "Provided transfer is invalid");
				LOGGER.error("addAccount :: provided account is invalid, transfer={}", transfer);
				return createResponse(null, error, HttpStatus.BAD_REQUEST);
			}
			if (StringUtils.isEmpty(transfer.getFromAccount())) {
				error = new ApiError(HttpStatus.BAD_REQUEST, "invalid fromAccount", "invalid fromAccount");
				LOGGER.error("addAccount :: invalid fromAccount, fromAccount={}", transfer.getFromAccount());
				return createResponse(null, error, HttpStatus.BAD_REQUEST);
			}
			if (StringUtils.isEmpty(transfer.getToAccount())) {
				error = new ApiError(HttpStatus.BAD_REQUEST, "invalid toAccount", "invalid toAccount");
				LOGGER.error("addAccount :: invalid toAccount, toAccount={}", transfer.getToAccount());
				return createResponse(null, error, HttpStatus.BAD_REQUEST);
			}
			transferModel = transferService.addTransfer(transfer);
		} catch (TransferRestApiException e) {
			LOGGER.error("addAccount :: There was an error adding Account", e);
			throw e;
		} catch ( Exception e) {
			LOGGER.error("addAccount :: There was an exception in the account service", e);
			throw e;
		}
		return createResponse(transferModel, error, HttpStatus.CREATED);
	}
}
