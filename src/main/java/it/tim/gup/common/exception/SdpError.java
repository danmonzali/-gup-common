package it.tim.gup.common.exception;

public class SdpError extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;
	private String type;
	private String statusCode;
	private String responseBody = "";
	private String description;
	private String path;
	private int timeout;

	public SdpError(String message, String type) {
		super();
		this.message = message;
		this.type = type;
	}

	public SdpError(String message, String type, String statusCode, String responseBody, String description, String path, int timeout) {
		super();
		this.message = message;
		this.type = type;
		this.statusCode = statusCode;
		this.responseBody = responseBody;
		this.description = description;
		this.path = path;
		this.timeout = timeout;
	}

	public SdpError(String message, String type, String statusCode) {
		super();
		this.message = message;
		this.type = type;
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message.replaceAll("[\n\r]", "");
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatusCode() {
		return statusCode.replaceAll("[\n\r]", "");
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponseBody() {
		return responseBody.replaceAll("[\n\r]", "");
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getDescription() {
		String buffer = "";
		buffer = description.length() >= 4000 ? description.substring(0, 4000) : description;
		return buffer.replaceAll("[\n\r]", "");
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
