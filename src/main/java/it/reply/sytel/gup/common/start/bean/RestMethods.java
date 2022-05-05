//package it.reply.sytel.gup.common.start.bean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RestMethods {
//
//	public List<RestMethod> restMethods;
//	
//	public void addRestMethod(String url, String method) {
//		getRestMethods().add(new RestMethod(url, method));
//	}
//
//	public List<RestMethod> getRestMethods() {
//		if (restMethods==null) {
//			restMethods = new ArrayList<RestMethods.RestMethod>();
//		}
//		return restMethods;
//	}
//
//	public void setRestMethods(List<RestMethod> restMethods) {
//		this.restMethods = restMethods;
//	}
//
//	public class RestMethod {
//		public String restUrl;
//		public String httpMethod;
//
//		public String getRestUrl() {
//			return restUrl;
//		}
//
//		public void setRestUrl(String restUrl) {
//			this.restUrl = restUrl;
//		}
//
//		public String getHttpMethod() {
//			return httpMethod;
//		}
//
//		public void setHttpMethod(String httpMethod) {
//			this.httpMethod = httpMethod;
//		}
//
//		public RestMethod(String restUrl, String httpMethod) {
//			super();
//			this.restUrl = restUrl;
//			this.httpMethod = httpMethod;
//		}
//
//	}
//
//}
