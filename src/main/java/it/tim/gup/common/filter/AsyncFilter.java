package it.tim.gup.common.filter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;

import it.tim.gup.common.util.GupContext;
import it.tim.gup.common.util.TIDUtil;


public abstract class AsyncFilter implements Filter {

	private static final Logger logger = LogManager.getLogger(GupFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		try {


			Date startDate = new Date();
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			httpServletResponse.addHeader("apigwRequestId", httpServletRequest.getHeader("APIGW_requestID"));

			MDC.put("gupSource", httpServletRequest.getHeader("sourceSystem"));
			MDC.put("gupSourceChannel", httpServletRequest.getHeader("channel"));
			MDC.put("gupId", httpServletRequest.getHeader("businessID"));
			MDC.put("gupCodServiceHxx", httpServletRequest.getHeader("codiceServizioHxx"));
			ThreadContext.put("gupApplicationComponent", getGupApplicationComponent());
			ThreadContext.put("gupEventType", getGupEventType());

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.SSS");  

			MDC.put("startDate", dateFormat.format(startDate));

			String tid = new TIDUtil().toString();
			GupContext.setTID(tid);

			MDC.put("gupTid", GupContext.getTID());

			filterChain.doFilter(request, response);

		} catch (Throwable e) {
			logger.error("doFilter - ERRORE ", e);
			throw e;
		} finally {
			MDC.clear();
		}
	}
	
	public abstract String getGupApplicationComponent();
	
	public abstract String getGupEventType();
}
