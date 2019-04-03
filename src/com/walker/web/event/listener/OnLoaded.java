package com.walker.web.event.listener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * spring 启动监听类
 *
 */
@Component
public class OnLoaded implements ApplicationListener<ContextRefreshedEvent> {
	static public Logger log = Logger.getLogger("OnLoaded"); 

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info(event.toString());
		// 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
		if(event.getApplicationContext().getParent() == null){
			//root application context 没有parent，他就是老大.
			log.info("##############################项目启动onApplicationEvent " + event.toString() + "###############");
			
			log.info("######################spring onload 启动完毕#######################");
			
			

			
		}
	}
}