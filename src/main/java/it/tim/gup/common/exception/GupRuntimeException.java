package it.tim.gup.common.exception;

public class GupRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GupRuntimeException(String id) {
		super(id);
	}
	
}
