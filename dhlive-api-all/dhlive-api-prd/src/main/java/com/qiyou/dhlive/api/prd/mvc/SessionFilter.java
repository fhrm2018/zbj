package com.qiyou.dhlive.api.prd.mvc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yaozhong.framework.base.common.utils.EmptyUtil;


public class SessionFilter implements Filter {
	Logger logger = LoggerFactory.getLogger(SessionFilter.class);

	private static ThreadLocal<HttpSession> threadLocalHttpSession = new ThreadLocal<HttpSession>();

	private final static String SESSION_TYPE_PATTERNS = "sessionType";

	private static String category;

	public static HttpSession getHttpSession() {
		return threadLocalHttpSession.get();
	}
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		HttpServletResponse response = (HttpServletResponse) arg1;
		HttpServletRequest req = (HttpServletRequest) request;
		if(EmptyUtil.isEmpty(req)) {
			return ;
		}
		if(EmptyUtil.isEmpty(response)) {
			return ;
		}
		SessionOperator.doSessionInfo(session, threadLocalHttpSession, req, response, category);
		arg2.doFilter(request, arg1);
	}

	public void init(FilterConfig arg0) throws ServletException {
		category = arg0.getInitParameter(SESSION_TYPE_PATTERNS);
		
	}

}
