package it.reply.sytel.gup.common.exception;

import org.springframework.http.HttpStatus;

public class GupSpecialHttpException extends GupException {

	private static final long serialVersionUID = 1L;

	private HttpStatus httpStatusCode;

	private String code;

	private String message;

	private String timeStamp;

	private String moreInfo;

	private String userMessage;

	public GupSpecialHttpException(HttpStatus httpStatusCode) {
		super();
		this.httpStatusCode = httpStatusCode;
	}

	public GupSpecialHttpException(HttpStatus httpStatusCode, String code, String message, String timeStamp) {
		super();
		this.httpStatusCode = httpStatusCode;
		this.code = code;
		this.message = message;
		this.timeStamp = timeStamp;
	}

	public GupSpecialHttpException(HttpStatus httpStatusCode, String code, String message, String timeStamp, String moreInfo, String userMessage) {
		super();
		this.httpStatusCode = httpStatusCode;
		this.code = code;
		this.message = message;
		this.timeStamp = timeStamp;
		this.moreInfo = moreInfo;
		this.userMessage = userMessage;
	}


	@Override
	public String getMessage() {
		return message;
	}

	public GupSpecialHttpException(String message) {
		super(message);
	}

	public HttpStatus getHttpStatusCode() {
		return this.httpStatusCode;
	}

	public void setHttpStatusCode(HttpStatus httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMoreInfo() {
		return moreInfo;
	}

	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
}
