package it.tim.gup.common.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

// TODO: Auto-generated Javadoc
/**
 * The Class ResponseHttpServer.
 */
public class ResponseHttpServer {

	/** The response code. */
	@JsonProperty(value = "responseCode", required = false)
	private String responseCode;
	
	/** The response description. */
	@JsonProperty(value = "responseDescription", required = false)
	private String responseDescription;

	/**
	 * Gets the response code.
	 *
	 * @return the response code
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * Sets the response code.
	 *
	 * @param responseCode the new response code
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * Gets the response description.
	 *
	 * @return the response description
	 */
	public String getResponseDescription() {
		return responseDescription;
	}

	/**
	 * Sets the response description.
	 *
	 * @param responseDescription the new response description
	 */
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResponseHttpServer [responseCode=" + responseCode + ", responseDescription=" + responseDescription + "]";
	}
	
}