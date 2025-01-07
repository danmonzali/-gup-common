package it.tim.gup.common.filter;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import it.tim.gup.common.util.GupContext;
import it.tim.gup.common.util.TIDUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class GupFilter implements Filter {

	private static final Logger logger = LogManager.getLogger(GupFilter.class);
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		try {
			
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			httpServletResponse.addHeader("apigwRequestId", httpServletRequest.getHeader("APIGW_requestID"));

			ThreadContext.put("gupSource", httpServletRequest.getHeader("sourceSystem"));
			ThreadContext.put("gupSourceChannel", httpServletRequest.getHeader("channel"));
			ThreadContext.put("gupId", httpServletRequest.getHeader("businessID"));
			ThreadContext.put("gupApplicationComponent", getGupApplicationComponent());
			ThreadContext.put("gupEventType", getGupEventType());
			
			String tid = new TIDUtil().toString();
			GupContext.setTID(tid);
			ThreadContext.put("gupTid", tid);
			
			filterChain.doFilter(request, response);
			
		} catch (Throwable e) {
			logger.error("doFilter - ERRORE ", e);
			throw e;
		} finally {
			ThreadContext.clearAll();
		}
	}
	
	public abstract String getGupApplicationComponent();
	
	public abstract String getGupEventType();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
