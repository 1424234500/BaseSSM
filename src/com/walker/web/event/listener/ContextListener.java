package com.walker.web.event.listener;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.walker.common.util.ClassUtil;
import com.walker.common.util.HttpUtil;
import com.walker.common.util.Page;
import com.walker.common.util.RobotUtil;
import com.walker.common.util.ThreadUtil;
import com.walker.common.util.Tools;
import com.walker.common.util.ThreadUtil.Type;
import com.walker.core.cache.Cache;
import com.walker.core.cache.CacheMgr;
import com.walker.service.impl.FileServiceImpl;

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
        log.info(".........................................................");
        log.info(".........................................................");
        log.info("正在启动系统 ... ...");
        ServletContext sc = sce.getServletContext();
        // 获取系统真实路径
        String systemPath = sc.getRealPath("/");
        if (!systemPath.endsWith(File.separator)) {
            systemPath += ",";
        }
//        String contextPath = sce.getContextPath();
        String contextPath = "/";
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

        startComp();
        startTestSelf();

        addShutdownHook();	//添加java程序关闭监听
        log.info("系统初始化完毕，开始接收请求！");
        log.info("........................................................");
        log.info("........................................................");
        log.info("........................................................");

        
    }
    private void addShutdownHook() {
    	Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	log.warn("----------销毁 执行ShutdownHook----------");
            	String nowTime = Tools.getNowTimeL();
            	String str = RobotUtil.getRuntime();
            	log.warn(nowTime);
            	log.warn(str);
            	log.warn("----------销毁 执行ShutdownHook 完毕----------");
            }
        });
		
	}
	/**
     * 挂载组件 初始化模块
     */
    private void startComp(){
        log.info("&&&&&&&&&&&&&&&&&&&&& 开始初始化call调用 &&&&&&&&&&&&&&&&&&&&");

        //初始化文件目录
		new FileServiceImpl().initDirs();
		
		//初始化缓存配置
		Cache<String> cache = CacheMgr.getInstance();
		String str = cache.get("on_list_start", ""); //来源于*.properties
//		onstart=util.cache.CacheMgr,util.annotation.TrackerMgr
		String[] arr = str.split(",");
		int i = 1;
		for(final String clz : arr){
			final int ii = i++;
			//使用缓冲队列任务的形式来 隔离 避免runtimeException 和相干
			ThreadUtil.execute(Type.SingleThread, new Runnable(){
				public void run(){
					log.info("*******************************************");
					log.info("********** step." + ii + "\t " + clz + ".call()");
					log.info("*******************************************");
					ClassUtil.doClassMethod(clz, "call");
				}
			});
		}
		
		log.info("&&&&&&&&&&&&&&&&&&&&&&& &&! 开始初始化call调用 &&&&&&&&&&&&&&&&&&&&&&");
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
		}, 10, TimeUnit.SECONDS);

		
	}

	@Override
    public void contextDestroyed(ServletContextEvent arg0) {
        log.info("...... ...................................................");
        log.info("...... ...................................................");
        log.info("...... ...................................................");
    	log.info("系统关闭！");
        log.info("...... ...................................................");
        log.info("...... ...................................................");
        log.info("...... ...................................................");


    }
}
