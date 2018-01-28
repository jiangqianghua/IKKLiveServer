package com.jiang.im.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

	/**
	 * 设置cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param maxage
	 */
	public static void set(HttpServletResponse response,
							String name,
							String value,
							int maxage){
		Cookie cookie = new Cookie("token",value);
		cookie.setPath("/");
		cookie.setMaxAge(maxage);
		response.addCookie(cookie);
	}
	/**
	 * 获取cookie
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie get(HttpServletRequest request,String name){
		
		Map<String, Cookie> cookMap = readCookieMap(request);
		if(cookMap.containsKey(name)){
			return cookMap.get(name);
		}
		return null;
	}
	
	private 	static Map<String, Cookie> readCookieMap(HttpServletRequest request){
		Map<String , Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie:cookies){
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}
}
