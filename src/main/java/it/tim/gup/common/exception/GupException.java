package it.tim.gup.common.exception;

public class GupException extends Exception {

	private static final long serialVersionUID = 1L;

	public GupException() {
		super();
	}
	
	public GupException(String message) {
		super(message);
	}

	public GupException(Throwable cause) {
		super(cause);
	}
	
}
