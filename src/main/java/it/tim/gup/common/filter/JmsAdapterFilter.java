package it.tim.gup.common.filter;

import java.util.Map;


import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import it.tim.gup.common.util.GupContext;
import it.tim.gup.common.util.TIDUtil;

@Component
public abstract class JmsAdapterFilter {

//	private final String _SYSTEM = "SYSTEM";
//	private final String _TID = "TID";

	public void fillContext(
			Map<String, Object> headers) {

//		ThreadContext.put("gupSource", (String) headers.get(this._SYSTEM)); 
		ThreadContext.put("gupApplicationComponent", getGupApplicationComponent());
		ThreadContext.put("gupEventType", getGupEventType());
//		ThreadContext.put("gupId",  (String)headers.get(this._TID)); -- id_univoco
		
		String tid = new TIDUtil().toString();
		GupContext.setTID(tid);
		ThreadContext.put("gupTid", tid);

	}

	public void clearContext() {
		ThreadContext.clearAll();
	}

	public abstract String getGupApplicationComponent();

	public abstract String getGupEventType();

}
