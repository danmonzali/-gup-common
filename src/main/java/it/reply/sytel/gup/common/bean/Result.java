package it.reply.sytel.gup.common.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

// TODO: Auto-generated Javadoc
/**
 * The Class Result.
 */
public class Result {

	/** The result code. */
	@JsonProperty(value = "resultCode", required = true)
	private String resultCode;
	
	/** The result description. */
	@JsonProperty(value = "resultDescription", required = true)
	private String resultDescription;

	/**
	 * Gets the result code.
	 *
	 * @return the result code
	 */
	public String getResultCode() {
		return resultCode;
	}

	/**
	 * Sets the result code.
	 *
	 * @param resultCode the new result code
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * Gets the result description.
	 *
	 * @return the result description
	 */
	public String getResultDescription() {
		return resultDescription;
	}

	/**
	 * Sets the result description.
	 *
	 * @param resultDescription the new result description
	 */
	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Result [resultCode=" + resultCode + ", resultDescription=" + resultDescription + "]";
	}
	
}
