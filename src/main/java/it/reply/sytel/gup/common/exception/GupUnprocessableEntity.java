package it.reply.sytel.gup.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class GupUnprocessableEntity extends GupRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public GupUnprocessableEntity(String id) {
		super(id);
	}
}
