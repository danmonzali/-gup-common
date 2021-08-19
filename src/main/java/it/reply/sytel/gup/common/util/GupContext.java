package it.reply.sytel.gup.common.util;

public class GupContext {

	private static ThreadLocal<String> tl;

	static {
		tl = new ThreadLocal<String>();
	}
	
	public static String getTID() {
		return tl.get();
	}
	
	public static void setTID(String TID) {
		tl.set(TID);
	}
	
}
