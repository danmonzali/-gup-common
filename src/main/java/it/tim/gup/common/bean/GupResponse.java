package it.tim.gup.common.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class GupResponse<T extends IGupServerResponse> {
	
	@JsonProperty(value="result", required=true)
	private Result result;

	@JsonProperty(value="responseHttpServer", required=false)
	private ResponseHttpServer responseHttpServer;
	
	@JsonProperty(value="responseServer", required=false)
	private T responseServer;

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public ResponseHttpServer getResponseHttpServer() {
		return responseHttpServer;
	}

	public void setResponseHttpServer(ResponseHttpServer responseHttpServer) {
		this.responseHttpServer = responseHttpServer;
	}

	public T getResponseServer() {
		return responseServer;
	}

	public void setResponseServer(T responseServer) {
		this.responseServer = responseServer;
	}

	@Override
	public String toString() {
		return "GupResponse [result=" + result + ", responseHttpServer=" + responseHttpServer + ", responseServer=" + responseServer + "]";
	}
	
}
