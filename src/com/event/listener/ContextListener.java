package com.event.listener;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.controller.Page;
import com.service.impl.FileServiceImpl;

import util.ClassUtil;
import util.HttpUtil;
import util.ThreadUtil;
import util.Tools;
import util.cache.Cache;
import util.cache.CacheMgr;

/**
 * 启动listener类，用于系统环境总体初始化
 * 
 * 迟于springmvc onload执行
 * 
 */
public class ContextListener implements ServletContextListener {
	private static Logger log = Logger.getLogger("ContextListener"); 

    /**
     * 初始化系统
     * @param sce 存放于WEB.XML中的配置信息
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 加载配置参数
        log.info(".........................................................");
        log.info("正在启动系统 ... ...");
        ServletContext sc = sce.getServletContext();
        // 获取系统真实路径
        String systemPath = sc.getRealPath("/");
        
        if (!systemPath.endsWith(File.separator)) {
            systemPath += ",";
        }
        
        String contextPath = sc.getContextPath();
        if (contextPath.equals("/")) {
            contextPath = "/";
        } else if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }
        
        Cache<String> cache = CacheMgr.getInstance();
        cache.put("系统工作目录", systemPath);
        cache.put("系统服务路径", contextPath);
        cache.put("系统启动时间", Tools.getNowTimeLS());
        
        log.info("系统工作目录: " + systemPath);
        log.info("系统服务路径: " + contextPath);

        
        log.info("&& 开始初始化call调用");
		new FileServiceImpl().initDirs();
		
		
		ClassUtil.doClassMethod("util.cache.CacheMgr", "call");
		ClassUtil.doClassMethod("util.annotation.TrackerMgr", "call");
		
		log.info("&&! 开始初始化call调用");

        startTestSelf();

        log.info("系统初始化完毕，开始接收请求！");
        log.info("...... ...................................................");

        
    }
    /**
     * 用以触发springmvc的代理 初始化
     */
    private void startTestSelf() {
    	ThreadUtil.schedule(new Runnable() {
			@Override
			public void run() { 
				log.info("######################开启延时测试初始化springMvc#######################");
				try {
					log.info(HttpUtil.post("http://localhost:8088/BaseSSM/tomcat/listCacheMap.do", new Page().toBean()));
					log.info("######################测试地址完毕#######################");
				} catch (Exception e) {
					log.info("地址测试异常 等会儿重新测试");
					startTestSelf();
				}
			}
		}, 10, TimeUnit.MILLISECONDS);

		
	}

	@Override
    public void contextDestroyed(ServletContextEvent arg0) {
    	
    	log.info("系统关闭！");
        log.info("...... ...................................................");


    }
}
