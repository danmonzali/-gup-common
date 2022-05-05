//package it.reply.sytel.gup.common.start.bean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RabbitBean {
//
//	public String uri;
//	public List<RabbitExchange> rabbitExchanges;
//
//	public void addRabbitExchange(String key, String value) {
//		getRabbitExchanges().add(new RabbitExchange(key, value));
//	}
//
//	public String getUri() {
//		return uri;
//	}
//
//	public void setUri(String uri) {
//		this.uri = uri;
//	}
//
//	public List<RabbitExchange> getRabbitExchanges() {
//		if (rabbitExchanges == null) {
//			rabbitExchanges = new ArrayList<RabbitBean.RabbitExchange>();
//		}
//		return rabbitExchanges;
//	}
//
//	public void setRabbitExchanges(List<RabbitExchange> rabbitExchanges) {
//		this.rabbitExchanges = rabbitExchanges;
//	}
//
//	public class RabbitExchange {
//		public String propertiesKey;
//		public String eventExchange;
//
//		public String getPropertiesKey() {
//			return propertiesKey;
//		}
//
//		public void setPropertiesKey(String propertiesKey) {
//			this.propertiesKey = propertiesKey;
//		}
//
//		public String getEventExchange() {
//			return eventExchange;
//		}
//
//		public void setEventExchange(String eventExchange) {
//			this.eventExchange = eventExchange;
//		}
//
//		public RabbitExchange(String propertiesKey, String eventExchange) {
//			super();
//			this.propertiesKey = propertiesKey;
//			this.eventExchange = eventExchange;
//		}
//
//	}
//
//}
