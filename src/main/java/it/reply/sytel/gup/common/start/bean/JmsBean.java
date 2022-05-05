//package it.reply.sytel.gup.common.start.bean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class JmsBean {
//
//	public String uriJms;
//
//	public List<JmsInner> jmsListeners;
//
//	public String getUriJms() {
//		return uriJms;
//	}
//
//	public void setUriJms(String uriJms) {
//		this.uriJms = uriJms;
//	}
//
//	public List<JmsInner> getJmsListeners() {
//		if (jmsListeners == null) {
//			jmsListeners = new ArrayList<JmsBean.JmsInner>();
//		}
//		return jmsListeners;
//	}
//
//	public void addJmsListenerIn(String in) {
//		getJmsListeners().add(new JmsInner(in, Boolean.TRUE));
//	}
//	public void addJmsListenerOut(String out) {
//		getJmsListeners().add(new JmsInner(out, Boolean.FALSE));
//	}
//
//	public void setJmsListeners(List<JmsInner> jmsListeners) {
//		this.jmsListeners = jmsListeners;
//	}
//
//	public class JmsInner {
//		public String nameIn;
//		public String nameOut;
//
//		public String getNameIn() {
//			return nameIn;
//		}
//
//		public void setNameIn(String nameIn) {
//			this.nameIn = nameIn;
//		}
//
//		public String getNameOut() {
//			return nameOut;
//		}
//
//		public void setNameOut(String nameOut) {
//			this.nameOut = nameOut;
//		}
//
//		public JmsInner(String nameListener, Boolean isIn) {
//			super();
//			if (isIn)
//				this.nameIn = nameListener;
//			else
//				this.nameOut = nameListener;
//		}
//
//	}
//}
