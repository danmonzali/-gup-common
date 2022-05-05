//package it.reply.sytel.gup.common.start.bean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class OracleBean {
//
//	private String connectionUrl;
//	private String host;
//	public List<String> oracleQueue;
//
//	public String getHost() {
//		return host;
//	}
//
//	public void setHost(String host) {
//		this.host = host;
//	}
//
//	public void addQueue(String queue) {
//		getOracleQueue().add(queue);
//	}
//
//	public List<String> getOracleQueue() {
//		if (oracleQueue == null) {
//			oracleQueue = new ArrayList<String>();
//		}
//		return oracleQueue;
//	}
//
//	public void setOracleQueue(List<String> oracleQueue) {
//		this.oracleQueue = oracleQueue;
//	}
//
//	public String getConnectionUrl() {
//		return connectionUrl;
//	}
//
//	private void setConnectionUrl(String connectionUrl) {
//		this.connectionUrl = connectionUrl;
//	}
//
//	public void addOracleConnection(String connectionValue) {
//		setHost(connectionValue.split("/")[1]);
//		setConnectionUrl(connectionValue);
//	}
//
//}
