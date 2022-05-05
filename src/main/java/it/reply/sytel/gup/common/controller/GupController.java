package it.reply.sytel.gup.common.controller;

import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sun.management.OperatingSystemMXBean;


@SuppressWarnings("restriction")
public abstract class GupController {
	
	@RequestMapping(value = "/probe", method = RequestMethod.GET)
	public void probe() {
		ThreadContext.put("gupDestination", "DIAGNOSTIC");
		OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		getLogger().info("Servizio probe invocato. " + "Used Memory  : " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes  " + "Free Memory  : " + Runtime.getRuntime().freeMemory() + " bytes  " + "Total Memory : " + Runtime.getRuntime().totalMemory() + " bytes  " + "Max Memory : " + Runtime.getRuntime().maxMemory() + " bytes  " + "System CPU Load : " + bean.getSystemCpuLoad() + " Proccess CPU Load :" + bean.getProcessCpuLoad());
		ThreadContext.remove("gupDestination");
	}
	
	@RequestMapping(value = "/getLocalTime", method = RequestMethod.GET)
	public String getLocalTime() {
		String localTime = new Timestamp(new Date().getTime()).toString();
		return localTime;
	}
	
	public RestHeader mappingHeader(String sourceSystem, String channel, String interactionDateDate,
			String interactionDateTime, String sessionID, String businessID, String transactionID, String messageID,
			String resubmitted, String APIGWRequestID) {
		RestHeader header = new RestHeader();
		header.setAPIGW_requestID(APIGWRequestID);
		header.setBusinessID(businessID);
		header.setChannel(channel);
		header.setInteractionDate_Date(interactionDateDate);
		header.setInteractionDate_Time(interactionDateTime);
		header.setMessageID(messageID);
		header.setResubmitted(resubmitted);
		header.setSessionID(sessionID);
		header.setSourceSystem(sourceSystem);
		header.setTransactionID(transactionID);
		return header;
	}

	
	protected abstract Logger getLogger();
	
}
