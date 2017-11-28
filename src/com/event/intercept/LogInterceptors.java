package com.event.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.mode.LoginUser;

import util.WebHelp;

/**
 * 拦截器 日志 登录/访问权限 事务  监控所有访问地址和参数打印 
 * @author Walker
 *
 */
public class LogInterceptors implements HandlerInterceptor{  
	static public Logger logger = LoggerFactory.getLogger("Log"); 

	
    /** 
     * 在渲染视图之后被调用； 
     * 可以用来释放资源 
     */   
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object method, Exception e)    throws Exception {  
    	// logger.info("==============执行顺序: 3、afterCompletion================");      
    }  
    /** 
     * 该方法在目标方法调用之后，渲染视图之前被调用； 
     * 可以对请求域中的属性或视图做出修改 
     *  
     */  
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object method, ModelAndView modelAndView) throws Exception {  
    	// logger.info("==============执行顺序: 2、postHandle================");    
    }  
  
    /** 
     * 可以考虑作权限，日志，事务等等 
     * 该方法在目标方法调用之前被调用； 
     * 若返回TURE,则继续调用后续的拦截器和目标方法 
     * 若返回FALSE,则不会调用后续的拦截器和目标方法 
     *  
     */  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object method) throws Exception {  
        //logger.info("==============执行顺序: 1、preHandle================");    
 
        String requestUri = request.getRequestURI();  
        String contextPath = request.getContextPath();  
        String url = requestUri.substring(contextPath.length());  //[/student/listm]
         
        String name = "", cla = "";
        if(method != null && method instanceof HandlerMethod){
        	try{
	        	HandlerMethod handlerMethod = (HandlerMethod) method; 
	        	name = handlerMethod.getMethod().getName();
	            cla = handlerMethod.getBean().toString();
	            cla = cla.substring(0, cla.indexOf("@"));
        	}catch(Exception e){ }
        }
        //日志 记录 输出       
	    logger.info("[" + url + "] [" + cla + "." + name + "]" + WebHelp.getRequestMap(request).toString());
 
        return true;  
    }  
  
}  