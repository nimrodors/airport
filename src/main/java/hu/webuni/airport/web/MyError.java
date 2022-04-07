package hu.webuni.airport.web;

import java.util.List;

import org.springframework.validation.FieldError;

//ez fog JSON-be visszautazni a törzsben
public class MyError {
	
	private String messages;
	private int errorCode;
	private List<FieldError> fieldErrors;
	
	public MyError(String messages, int errorCode) {
		super();
		this.messages = messages;
		this.errorCode = errorCode;
	}
	
	public MyError(String messages) {
		super();
		this.messages = messages;
	}

	public String getMessages() {
		return messages;
	}
	public void setMessages(String messages) {
		this.messages = messages;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}
	public void setFieldErrors(List<FieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
	
	public void addFieldError(String path, String messages) {
		FieldError error = new FieldError(path, messages, messages); 
		fieldErrors.add(error);
	}
}
