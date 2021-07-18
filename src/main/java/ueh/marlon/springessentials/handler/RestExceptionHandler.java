package ueh.marlon.springessentials.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ueh.marlon.springessentials.exception.BadRequestException;
import ueh.marlon.springessentials.exception.BadRequestExceptionDetails;
import ueh.marlon.springessentials.exception.ExceptionDetails;
import ueh.marlon.springessentials.exception.ValidationExceptionDetails;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(BadRequestException bre){
		return new ResponseEntity<>(
				BadRequestExceptionDetails.builder()
					.timestamp(LocalDateTime.now())
					.status(HttpStatus.BAD_REQUEST.value())
					.title("Bad Request Exception, Check the Documentation")
					.details(bre.getMessage())
					.developerMessage(bre.getClass().getName())
					.build(), 
					HttpStatus.BAD_REQUEST
				);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		
		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
		String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
		
		return new ResponseEntity<>(
				ValidationExceptionDetails.builder()
					.timestamp(LocalDateTime.now())
					.status(HttpStatus.BAD_REQUEST.value())
					.title("Bad Request Exception, Invalid Fields")
					.details("Check the fields error")
					.developerMessage(ex.getClass().getName())
					.fields(fields)
					.fieldsMessage(fieldsMessage)
					.build(), 
					HttpStatus.BAD_REQUEST
				);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

		ExceptionDetails exceptionDetails = ExceptionDetails.builder()
			.timestamp(LocalDateTime.now())
			.status(status.value())
			.title(ex.getMessage())
			.details("Check the fields error")
			.developerMessage(ex.getClass().getName())
			.build();
		
		return new ResponseEntity<>(exceptionDetails, headers, status);
	}
	
	
}
