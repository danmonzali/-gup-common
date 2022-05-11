package it.tim.gup.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
public class GupGatewayTimeOut extends RuntimeException  {

private static final long serialVersionUID = 1L;
	
	public GupGatewayTimeOut(String id) {
		super(id);
	}

}
