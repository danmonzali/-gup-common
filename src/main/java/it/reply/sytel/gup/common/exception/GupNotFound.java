package it.reply.sytel.gup.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class GupNotFound extends RuntimeException  {

	private static final long serialVersionUID = 1L;
	
	public GupNotFound(String id) {
		super(id);
	}

	
}
