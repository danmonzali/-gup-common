package it.reply.sytel.gup.common.exception;

import org.springframework.http.HttpStatus;

public class GupHttpException extends GupException{

	private static final long serialVersionUID = 1L;
	
	private String status;
	
	private String message;
	
	private HttpStatus statusCode;
	
	private String responseBody="";
	
	public GupHttpException(String message, String status) {
		super(message);
		this.message = message;
		this.status = status;
	}

	public GupHttpException(HttpStatus statusCode) {
		super();
		this.statusCode = statusCode;
	}
	
	public GupHttpException(HttpStatus statusCode, String message) {
		this.message = message;
		this.statusCode = statusCode;
	}
	
	
	
	public GupHttpException(String message, HttpStatus statusCode, String responseBody) {
		super();
		this.message = message;
		this.statusCode = statusCode;
		this.responseBody = responseBody;
	}

	public GupHttpException(String message) {
		super(message);
	}

	public HttpStatus getStatusCode() {
		return this.statusCode;
	}
	
	public String getStatus() {
		return status;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponseBody() {
		if (responseBody !=null)
			return responseBody;
		else 
			return "";
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	
	
	
}
