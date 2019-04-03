package com.walker.web.event.intercept;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.walker.common.util.Bean;
import com.walker.core.cache.Cache;
import com.walker.core.cache.CacheMgr;
import com.walker.service.LogService;
import com.walker.service.LoginService;
import com.walker.web.RequestUtil;

/**
 * 拦截器 日志 登录/访问权限 事务   监控所有用户操作和登录并记录日志数据库
 * @author Walker
 *
 */
public class LoginInterceptors implements HandlerInterceptor{  
	static public Logger logger = Logger.getLogger("Aop"); 

	Cache<String> cache = CacheMgr.getInstance();
    @Autowired
	LoginService loginService;
	
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
//	    LoginUser user = (LoginUser) request.getSession().getAttribute("SY_LOGINUSER");
	    
		Object tokenObj = request.getSession().getAttribute("token");
//		cache.put("USER_ONLINE", map);
	    Map<String, Object> map =  cache.get(LoginService.CACHE_KEY, new HashMap<String, Object>());
	    if(tokenObj != null && map != null && map.containsKey(tokenObj) ){
	    	Bean user = (Bean) map.get(tokenObj);
	    	
	    	String token = user.get("token", "");
	    	String id = user.get("id", "");
	    	Long time = user.get("time", 0L);
	    	Long expire = user.get("expire", 0L);
	    	if(time + expire < System.currentTimeMillis()){
		    	logger.info(token + "." + id + "." + time + "." + expire + "." + " 过期 失效 ");
		    	loginService.login();
	    	}else{
		    	logger.info(token + "." + id + "." + time + "." + expire + "." + " 已登录 未过期 ");
		    	//登录用户操作日志 记录 用户id,操作url权限?,用户操作ip/mac/端口
		    	String requestUri = request.getRequestURI();  
		        String contextPath = request.getContextPath();  
		        String url = requestUri.substring(contextPath.length());  //[/student/listm]
		        //sequenceid time userid url ip host 端口     
		        String ip = request.getRemoteAddr();//返回发出请求的IP地址
		        String params = RequestUtil.getRequestBean(request).toString();
		        String host=request.getRemoteHost();//返回发出请求的客户机的主机名
		        int port =request.getRemotePort();//返回发出请求的客户机的端口号。
		        
		        logService.userMake(id, url, ip, host, port, params);
	    	}
    	}else{
	    	logger.info("token:" + tokenObj + " 无效 未登录：跳转到login页面！");
	    	loginService.login();
           // request.getRequestDispatcher("/login/onlogin.do").forward(request, response);  
           // return false;
	    }
        return true;  
    }  
  
}  