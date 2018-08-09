package com.controller;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import util.Bean;
import util.Tools;

/**
 * servlet request response printwriter 帮助工具
 * @author Walker
 *
 */
public class WebHelp {

	/**
	 * 获取request所有参数Bean
	 * @param request
	 * @return Bean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Bean getRequestBean(HttpServletRequest request){
		Bean res = new Bean();
		Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String name=(String)enu.nextElement();  
			String value = WebHelp.getKey(request, name);
			res.put(name, value);
		}
		return res;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Bean getParam(HttpServletRequest request, List<Object> colNames){
		Bean res = new Bean();
		for(Object key : colNames){
			res.put(key, WebHelp.getKey(request, key));
		}
		return res;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Bean getParam(HttpServletRequest request, Object...colNames){
		Bean res = new Bean();
		for(Object key : colNames){
			res.put(key, WebHelp.getKey(request, key));
		}
		return res;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Bean getKeyParam(HttpServletRequest request, String keyName){
		Bean res = new Bean();
		res.put(keyName, request.getParameter(keyName));
		return res;
	}
	@SuppressWarnings({ })
	public static String getKey(HttpServletRequest request, Object name){
		String value = request.getParameter((String)name);
		if(!Tools.notNull(value)){	//兼容全小写
			value = request.getParameter(((String)name).toLowerCase());
		}
		if(!Tools.notNull(value)){	//兼容全大写
			value = request.getParameter(((String)name).toUpperCase());
		}
		if(value == null)value = "";
		return value;
	}
	
	
}