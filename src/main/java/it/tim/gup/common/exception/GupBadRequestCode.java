package it.tim.gup.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GupBadRequestCode extends RuntimeException  {

	private String message;
	private String code;
	
	private static final long serialVersionUID = 1L;
	
	public GupBadRequestCode(String message, String code) {
		this.message = message;
		this.code = code;	
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


}
