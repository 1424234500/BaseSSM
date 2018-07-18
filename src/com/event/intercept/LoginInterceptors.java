package com.event.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.controller.WebHelp;
import com.mode.LoginUser;
import com.service.LogService;

/**
 * 拦截器 日志 登录/访问权限 事务   监控所有用户操作和登录并记录日志数据库
 * @author Walker
 *
 */
public class LoginInterceptors implements HandlerInterceptor{  
	static public Logger logger = LoggerFactory.getLogger("Aop"); 

	
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
  
    @Autowired
	@Qualifier("logService") 
    LogService logService;
    /** 
     * 可以考虑作权限，日志，事务等等 
     * 该方法在目标方法调用之前被调用； 
     * 若返回TURE,则继续调用后续的拦截器和目标方法 
     * 若返回FALSE,则不会调用后续的拦截器和目标方法 
     *  
     */  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object method) throws Exception {  
       // logger.info("==============执行顺序: 1、preHandle================");    
 
        //登录权限
	    LoginUser user = (LoginUser) request.getSession().getAttribute("SY_LOGINUSER");
	    if(user != null){
	    	logger.info(user.toString());
	    	//登录用户操作日志 记录 用户id,操作url权限?,用户操作ip/mac/端口
	    	String requestUri = request.getRequestURI();  
	        String contextPath = request.getContextPath();  
	        String url = requestUri.substring(contextPath.length());  //[/student/listm]
	        //sequenceid time userid url ip host 端口     
	        String ip = request.getRemoteAddr();//返回发出请求的IP地址
	        String params = WebHelp.getRequestMap(request).toString();
	        String host=request.getRemoteHost();//返回发出请求的客户机的主机名
	        int port =request.getRemotePort();//返回发出请求的客户机的端口号。
	        
	        logService.userMake(user.getId(), url, ip, host, port, params);
	        
    	}else{
//	    	logger.info("未登录：跳转到login页面！");  
           // request.getRequestDispatcher("/login/onlogin.do").forward(request, response);  
           // return false;
	    }
        return true;  
    }  
  
}  