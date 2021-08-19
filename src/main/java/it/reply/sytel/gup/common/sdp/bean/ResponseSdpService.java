package it.reply.sytel.gup.common.sdp.bean;

import org.springframework.http.ResponseEntity;

public class ResponseSdpService {

	private ResponseEntity<String> entity;
	private String buffer = "";

	public ResponseEntity<String> getEntity() {
		return entity;
	}

	public void setEntity(ResponseEntity<String> entity) {
		this.entity = entity;
	}

	public String getBuffer() {
		buffer = buffer.length() >= 4000 ? buffer.substring(0, 4000) : buffer;
		return buffer.replaceAll("[\n\r]", "");
	}

	public void setBuffer(String buffer) {
		this.buffer = buffer;
	}

	public ResponseSdpService(ResponseEntity<String> entity, String buffer) {

		this.entity = entity;
		this.buffer = buffer;
	}

	public ResponseSdpService() {
		
	}
}
