package com.mms.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeFilter;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilterWrapper;

public class XssServletFilter implements Filter {

	private String[] acceptUrls;

	public XssServletFilter(String[] acceptUrls) {
		this.acceptUrls = acceptUrls;
	}

	private XssEscapeFilter xssEscapeFilter = XssEscapeFilter.getInstance();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (excludeUrl(request)) {
			chain.doFilter(request, response); // 걸러내는 URI일 경우 요청값 그대로 처리
		} else {
			chain.doFilter(new XssEscapeServletFilterWrapper(request, xssEscapeFilter), response);
		}
	}

	private boolean excludeUrl(ServletRequest request) {

		String uri = ((HttpServletRequest) request).getRequestURI().toString().trim();
		boolean returnValue = false;
		for (String url : this.acceptUrls) {
			if (uri.contains(url)) {
				returnValue = true;
			}
		}

		return returnValue;
	}

	@Override
	public void destroy() {
	}

}