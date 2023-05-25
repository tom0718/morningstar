package com.mms.util;

import javax.servlet.http.HttpServletRequest;

public class GetIpAddress {
	
	
	public static String getIp(HttpServletRequest req) {
		
		String ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null) {
			ip = req.getRemoteAddr();
		}
		
		return ip;
	}
	
}
