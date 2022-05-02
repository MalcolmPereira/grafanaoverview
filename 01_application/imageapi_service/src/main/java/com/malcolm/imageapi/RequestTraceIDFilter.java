package com.malcolm.imageapi;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Simple Request Tracing Filter. This will generate a UUID and place it in the logging 
 * Thread context to trace request by UUID. The same trace id is added to the response header.  
 * 
 * @author Malcolm
 *
 */
@Component
public class RequestTraceIDFilter extends OncePerRequestFilter {

	public static final String TRACE_ID = "TRACE_ID";

	public static final String REQUEST_ID = "REQUEST_ID";

	public static final String REQUEST_ID_HEADER = "REQUEST-ID";

	public static final String X_REQUEST_ID_HEADER = "X-Request-ID";

	private static final Logger LOGGER = LogManager.getLogger(RequestTraceIDFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (HttpMethod.TRACE.name().equals(request.getMethod())
				|| HttpMethod.OPTIONS.name().equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return;
		}
		try {
			if(request.getHeader(REQUEST_ID_HEADER)!= null){
				ThreadContext.put(REQUEST_ID, request.getHeader(REQUEST_ID_HEADER));
			}else if(request.getHeader(X_REQUEST_ID_HEADER)!= null){
				ThreadContext.put(REQUEST_ID, request.getHeader(X_REQUEST_ID_HEADER));
			}

			final String requestTraceID = UUID.randomUUID().toString();
			ThreadContext.put(TRACE_ID, requestTraceID);
			response.addHeader(TRACE_ID, requestTraceID);
			filterChain.doFilter(request, response);

		} finally {
			ThreadContext.remove(TRACE_ID);
			ThreadContext.remove(REQUEST_ID);
		}
	}

	private final String generateUUID() {
		return UUID.randomUUID().toString() + "-" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
	}
}
