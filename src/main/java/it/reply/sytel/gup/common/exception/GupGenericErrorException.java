package it.reply.sytel.gup.common.exception;

public class GupGenericErrorException extends GupException{

	private static final long serialVersionUID = 1L;
	private int statusCode;	
	
	
	public GupGenericErrorException(int statusCode,  String message) {
        super(message);
        this.statusCode = statusCode;
    }
	

    public int getStatusCode() {
        return this.statusCode;
    }


}
