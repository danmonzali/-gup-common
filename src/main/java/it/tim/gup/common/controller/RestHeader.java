package it.tim.gup.common.controller;

public class RestHeader {

	private String sourceSystem;
	private String channel;
	private String interactionDate_Date;
	private String interactionDate_Time;
	private String sessionID;
	private String businessID;
	private String transactionID;
	private String messageID;
	private String resubmitted;
	private String APIGW_requestID;

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getInteractionDate_Date() {
		return interactionDate_Date;
	}

	public void setInteractionDate_Date(String interactionDate_Date) {
		this.interactionDate_Date = interactionDate_Date;
	}

	public String getInteractionDate_Time() {
		return interactionDate_Time;
	}

	public void setInteractionDate_Time(String interactionDate_Time) {
		this.interactionDate_Time = interactionDate_Time;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getBusinessID() {
		return businessID;
	}

	public void setBusinessID(String businessID) {
		this.businessID = businessID;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getResubmitted() {
		return resubmitted;
	}

	public void setResubmitted(String resubmitted) {
		this.resubmitted = resubmitted;
	}

	public String getAPIGW_requestID() {
		return APIGW_requestID;
	}

	public void setAPIGW_requestID(String aPIGW_requestID) {
		APIGW_requestID = aPIGW_requestID;
	}

}
