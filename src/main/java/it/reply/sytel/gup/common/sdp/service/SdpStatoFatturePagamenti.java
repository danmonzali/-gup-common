package it.reply.sytel.gup.common.sdp.service;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import it.reply.sytel.gup.common.sdp.bean.StatoFatturePagamenti;
import it.reply.sytel.gup.common.service.RestCallService;

@Service
public abstract class SdpStatoFatturePagamenti extends RestCallService {

	@Value("${gup.sdp.statoFatture.pagamenti.protocol}")
	private String protocolSdp;

	@Value("${gup.sdp.statoFatture.pagamenti.host}")
	private String hostSdp;

	@Value("${gup.sdp.statoFatture.pagamenti.port}")
	private String portSdp;

	@Value("${gup.sdp.statoFatture.pagamenti.path}")
	private String path;

	@Value("${gup.sdp.statoFatture.pagamenti.timeout}")
	private int timeout;

	public ResponseEntity<String> callSdpStatoFatturePagamenti(StatoFatturePagamenti sfp) {
		ResponseEntity<String> response = null;
		try {
			putLogThreadContextDestination();
			HttpHeaders headers = new HttpHeaders();
			HttpMethod httpMethod = HttpMethod.POST;
			String url = protocolSdp + "://" + hostSdp + ":" + portSdp + path;
			String body = sfp.getInputPagamenti().toJsonString();
			headers.set(HttpHeaders.ACCEPT, "application/json");
			headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
			headers.set("sourceSystem", "GUP");
			headers.set("channel", sfp.getChannel());
			headers.set("interactionDate-Date", sfp.getInteractionDateDate());
			headers.set("interactionDate-Time", sfp.getInteractionDateTime());
			headers.set("sessionID", sfp.getSessionID());
			headers.set("businessID", sfp.getBusinessID());
			headers.set("transactionID", sfp.getTransactionID());
			headers.set("messageID", sfp.getMessageID());
			if (sfp.getAPIGW_requestID() != null) {
				headers.set("APIGW_requestID", sfp.getAPIGW_requestID());
			}
			if (sfp.getResubmitted() != null) {
				headers.set("resubmitted", sfp.getResubmitted());
			}
			
			HttpEntity<String> entity = new HttpEntity<>(body, headers);
			timeout = (timeout == 0) ? 1 : timeout;
			getLogger().info("callSdpStatoFatturePagamenti - ### CALL SDP ### - invocazione SDP - url = " + url + " con path variable [rifCliente] = " + sfp.getRifCliente());
			getLogger().info("callSdpStatoFatturePagamenti - ### CALL SDP ### - headers e body = " + entity);
			RestTemplate restTemplate = getRestTemplate(protocolSdp, timeout);
			response = restTemplate.exchange(url, httpMethod, entity, String.class, sfp.getRifCliente());

			getLogger().info("callSdpStatoFatturePagamenti - ### CALL SDP ### - response SDP = " + response);
		} finally {
			removeLogThreadContextDestination();
		}
		return response;
	}

	@Override
	protected void putLogThreadContextDestination() {
		ThreadContext.put("gupDestination", hostSdp);
	}

	@Override
	protected void removeLogThreadContextDestination() {
		ThreadContext.remove("gupDestination");
	}

}
