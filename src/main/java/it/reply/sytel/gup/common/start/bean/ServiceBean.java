//package it.reply.sytel.gup.common.start.bean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ServiceBean {
//
//	public List<Service> services;
//
//	public List<Service> getServices() {
//		if (services == null) {
//			services = new ArrayList<ServiceBean.Service>();
//		}
//		return services;
//	}
//
//	public void addService(String host, String path) {
//		getServices().add(new Service(host, path));
//	}
//
//	public void setServices(List<Service> services) {
//		this.services = services;
//	}
//
//	public final class Service {
//		public String host;
//		public String path;
//
//		public String getHost() {
//			return host;
//		}
//
//		public void setHost(String host) {
//			this.host = host;
//		}
//
//		public String getPath() {
//			return path;
//		}
//
//		public void setPath(String path) {
//			this.path = path;
//		}
//
//		public Service(String host, String path) {
//			super();
//			this.host = host;
//			this.path = path;
//		}
//
//	}
//}
