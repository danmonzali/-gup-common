package it.tim.gup.common.sdp.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatoFatturePagamenti implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value="sourceSystem", required=true)
	private String sourceSystem;
	
	@JsonProperty(value="channel", required=true)
	private String channel;
	
	@JsonProperty(value="interactionDateDate", required=true)
	private String interactionDateDate;
	
	@JsonProperty(value="interactionDateTime", required=true)
	private String interactionDateTime;	
	
	@JsonProperty(value="sessionID", required=true)
	private String sessionID;	
	
	@JsonProperty(value="businessID", required=true)
	private String businessID;	
	
	@JsonProperty(value="transactionID", required=true)
	private String transactionID;	
	
	@JsonProperty(value="messageID", required=true)
	private String messageID;	
	
	@JsonProperty(value="APIGW_requestID", required=false)
	private String APIGW_requestID;
	
	@JsonProperty(value="resubmitted", required=false)
	private String resubmitted;
	
	@JsonProperty(value="internalTid", required=false)
	private String internalTid;
	
	@JsonProperty(value="internalCaller", required=false)
	private String internalCaller;
	
	@JsonProperty(value="rifCliente", required=true)
	private String rifCliente;
	
	@JsonProperty(value="inputPagamenti", required=true)
	private InputPagamenti inputPagamenti;
	
	public String getRifCliente() {
		return rifCliente;
	}

	public void setRifCliente(String rifCliente) {
		this.rifCliente = rifCliente;
	}

	public InputPagamenti getInputPagamenti() {
		return inputPagamenti;
	}

	public void setInputPagamenti(InputPagamenti inputPagamenti) {
		this.inputPagamenti = inputPagamenti;
	}

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

	public String getInteractionDateDate() {
		return interactionDateDate;
	}

	public void setInteractionDateDate(String interactionDateDate) {
		this.interactionDateDate = interactionDateDate;
	}

	public String getInteractionDateTime() {
		return interactionDateTime;
	}

	public void setInteractionDateTime(String interactionDateTime) {
		this.interactionDateTime = interactionDateTime;
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

	public String getAPIGW_requestID() {
		return APIGW_requestID;
	}

	public void setAPIGW_requestID(String aPIGW_requestID) {
		APIGW_requestID = aPIGW_requestID;
	}

	public String getResubmitted() {
		return resubmitted;
	}

	public void setResubmitted(String resubmitted) {
		this.resubmitted = resubmitted;
	}
	
	public String getInternalTid() {
		return internalTid;
	}

	public void setInternalTid(String internalTid) {
		this.internalTid = internalTid;
	}

	public String getInternalCaller() {
		return internalCaller;
	}

	public void setInternalCaller(String internalCaller) {
		this.internalCaller = internalCaller;
	}

	@Override
	public String toString() {
		return "MessageNotification [sourceSystem=" + sourceSystem + ", channel=" + channel + ", interactionDateDate=" + interactionDateDate + ", interactionDateTime=" + interactionDateTime + ", sessionID=" + sessionID + ", businessID=" + businessID + ", transactionID=" + transactionID + ", messageID=" + messageID + ", APIGW_requestID=" + APIGW_requestID + ", resubmitted=" + resubmitted + ", internalTid=" + internalTid + ", internalCaller=" + internalCaller + ", rifCliente=" + rifCliente + ", inputPagamenti=" + inputPagamenti + "]";
	}

	public String toJsonString() {
		StringBuffer json = new StringBuffer("{");
		json.append("\"sourceSystem\" : \"").append(sourceSystem).append("\", ");
		json.append("\"channel\" : \"").append(channel).append("\", ");
		json.append("\"interactionDateDate\" : \"").append(interactionDateDate).append("\", ");
		json.append("\"interactionDateTime\" : \"").append(interactionDateTime).append("\", ");
		json.append("\"sessionID\" : \"").append(sessionID).append("\", ");
		json.append("\"businessID\" : \"").append(businessID).append("\", ");
		json.append("\"transactionID\" : \"").append(transactionID).append("\", ");
		json.append("\"messageID\" : \"").append(messageID).append("\", ");
		if (APIGW_requestID != null && !APIGW_requestID.isEmpty()) {
			json.append("\"APIGW_requestID\" : \"").append(APIGW_requestID).append("\", ");
		}
		if (resubmitted != null && !resubmitted.isEmpty()) {
			json.append("\"resubmitted\" : \"").append(resubmitted).append("\", ");
		}
		json.append("\"internalTid\" : \"").append(internalTid).append("\", ");
		json.append("\"internalCaller\" : \"").append(internalCaller).append("\", ");
		json.append("\"rifCliente\" : \"").append(rifCliente).append("\", ");
		json.append("\"inputPagamenti\" : ").append(inputPagamenti.toJsonString());
		json.append("}");
		return json.toString();
	}
	
}
