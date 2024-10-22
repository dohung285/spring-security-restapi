package com.dohung.appdeveloper.security;

import com.dohung.appdeveloper.SpringApplicationContext;

public class SecurityConstant {
	public static final long EXPIRATION_TIME = 864000000; // 10 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";
	//public static final String TOKEN_SECRET = "jf9i4jgu83nfl0jfu57ejf7";
	
	public static String getTokenSecret() {
		AppProperties appProperties=(AppProperties) SpringApplicationContext.getBean("AppProperties");
		return appProperties.getTokenSecret();
	}
	

}
