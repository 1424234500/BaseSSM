package com.event.listener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.service.impl.FileServiceImpl;

import util.ClassUtil;

@Component
public class OnLoaded implements ApplicationListener<ContextRefreshedEvent> {
	static public Logger logger = Logger.getLogger("OnLoaded"); 

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info(event.toString());
		// 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
		if(event.getApplicationContext().getParent() == null){
			//root application context 没有parent，他就是老大.
			logger.info("##############################项目启动onApplicationEvent " + event.toString() + "###############");
			
			new FileServiceImpl().initDirs();
			
			
			ClassUtil.doClassMethod("util.cache.CacheMgr", "call");
			ClassUtil.doClassMethod("util.annotation.TrackerMgr", "call");
			
			
			logger.info("######################启动完毕#######################");
			
			
			logger.info("######################开启测试初始化springMvc#######################");
			
			
			
		}
	}
}