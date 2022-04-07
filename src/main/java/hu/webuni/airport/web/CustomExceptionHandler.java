package hu.webuni.airport.web;

import java.util.List;

import javax.management.BadAttributeValueExpException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import hu.webuni.airport.service.NonUniqueIataException;

//import static org.springframework.http.HttpStatus.BAD_REQUEST;

//Hiba üzenet kiírása formázva:
//https://stackoverflow.com/questions/67994305/implement-validation-message-for-default-size-validation

@RestControllerAdvice
public class CustomExceptionHandler {
	
	
	private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);


	@ExceptionHandler(NonUniqueIataException.class)
	public ResponseEntity<MyError> handleNonUniqueIata(NonUniqueIataException e, WebRequest req) {
		log.warn(e.getMessage(), e);;
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MyError(e.getMessage(), 1002));
	}
	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<MyError> handleValidationError(MethodArgumentNotValidException e, WebRequest req) {
//		log.warn(e.getMessage(), e);;
//		MyError myError = new MyError(e.getMessage(), 1002);
//		myError.setFieldErrors(e.getBindingResult().getFieldErrors());
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(myError);
//	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MyError> handleValidationError(MethodArgumentNotValidException e, WebRequest req) {
		BindingResult bindingResult = e.getBindingResult();
		MyError myError = null;
		for (FieldError f : bindingResult.getFieldErrors()) {
			myError = new MyError(f.getDefaultMessage().toUpperCase());
			
		}
		for (ObjectError o: bindingResult.getGlobalErrors()) {
			o.getDefaultMessage();
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(myError);
	}
	
//	@ResponseStatus(BAD_REQUEST)
//	@ResponseBody
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public MyError handleValidationError(MethodArgumentNotValidException e) {
//		BindingResult bindingResult = e.getBindingResult();
//		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//		return processFieldErrors(fieldErrors);
//	}
//	
//	public MyError processFieldErrors(List<FieldError> fieldErrors) {
//		MyError myError = new MyError("Hiba", BAD_REQUEST.value());
//		for (FieldError f : fieldErrors) {
//			myError.addFieldError(f.getField(), f.getDefaultMessage());
//		}
//		return myError;
//	}
}
