package com.raquibul.bank.transfer.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This is common ExceptionHanlder for the Rest API. All Generic and application level 
 * custom exception is handled by this handler.
 * @see ResponseEntityExceptionHandler
 * @author Raquibul Hasan 
 *
 */
@ControllerAdvice
public class TransferRestExceptionHandler extends ResponseEntityExceptionHandler {
	private static final String API_EXCEPTION_MESSAGE = "There was an exception occured and request could not be processed";
	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
        logger.error("handleHttpRequestMethodNotSupported :: There was an error", ex);
		
		StringBuilder message = new StringBuilder();
		message.append(ex.getMethod());
		message.append(" HTTP Request method is not supported for this request. Supported methods are:");
		ex.getSupportedHttpMethods().forEach(supportedMethod -> message.append(supportedMethod + " "));

		ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED,
				ex.getLocalizedMessage(), message.toString());
		return new ResponseEntity<Object>(apiError, new HttpHeaders(),
				apiError.getStatus());
	}
	/**
	 * Handle All generic exception thrown by the application
	 * @param exception - Generic Exception thrown
	 * @param request - the WebRequest
	 * @return {@link ResponseEntity} - the ResResponseEntity
	 */
	@ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAllException(Exception exception, final WebRequest request) {
        logger.error("handleAllException :: There was an error", exception);
        StringBuilder message = new StringBuilder();
        message.append("There was an exception of type [")
        .append(exception.getClass().getSimpleName()).append("] occured");
        
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage(), message.toString());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
	
	/**
	 * Handle All API level exception thrown by the application
	 * @param apiException
	 * @param request
	 * @return {@link ResponseEntity}
	 */
	@ExceptionHandler({ TransferRestApiException.class })
    public ResponseEntity<Object> handleApiException(final TransferRestApiException apiException, final WebRequest request) {
        logger.debug("handleApiException :: There was an error", apiException);
        
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, apiException.getLocalizedMessage(), API_EXCEPTION_MESSAGE);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
	
	/**
	 * Handle RequestValidation exception thrown by the application
	 * @param reqValException - the RequestValidationException
	 * @param request
	 * @return {@link ResponseEntity}
	 */
	@ExceptionHandler({ RequestValidationException.class })
    public ResponseEntity<Object> handleRequestValidationException(final RequestValidationException reqValException, final WebRequest request) {
        logger.debug("handleApiException :: There was an error", reqValException);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, reqValException.getLocalizedMessage(), "Request validation error");
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
	
	/**
	 * Handle RequestValidation exception thrown by the application
	 * @param resValException - the RequestValidationException
	 * @param request
	 * @return {@link ResponseEntity}
	 */
	@ExceptionHandler({ ResponseValidationException.class })
    public ResponseEntity<Object> handleResponseValidationException(final ResponseValidationException resValException, final WebRequest request) {
        logger.debug("handleApiException :: There was an error", resValException);
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, resValException.getLocalizedMessage(), "Response validation error");
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, final HttpHeaders headers, HttpStatus status,
			final WebRequest request) {
		
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "nohandler error");
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
}
