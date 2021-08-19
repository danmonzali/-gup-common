package it.reply.sytel.gup.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import it.reply.sytel.gup.common.util.GupContext;
import it.reply.sytel.gup.common.util.TIDUtil;

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
			logger.error("doFilter - ERRORE " + e.getMessage().replaceAll("[\n\r]", ""));
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
