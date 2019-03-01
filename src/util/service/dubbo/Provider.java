package util.service.dubbo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import util.Call;
import util.HttpUtil;
import util.ThreadUtil;
import util.service.service.ServiceDubbo;

/**
 * service provider
 *
 */
public class Provider implements Call{
	private static Logger log = Logger.getLogger(Provider.class); 

	/**
	 * 初始化服务
	 * 验证测试
	 */
	@Override
	public void call() {
		log.info("** 初始化 dubbo provider ---------------------- ");
//		com.alibaba.dubbo.container.Main.main(new String[] {"dubbo-provider.xml"});
		 
//		System.setProperty("java.net.preferIPv4Stack", "true");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "dubbo-provider.xml" });
		context.start(); 
		log.info("**! 初始化完毕 dubbo provider------------------- ");
		
		log.info("-- 开始测试dubbo --------------");
		try {
//			  ServiceDubbo service = (ServiceDubbo)context.getBean("serviceDubbo");
//			  log.warn(service.sayHello("in args[]" ));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("test dubbo service error !" + e.toString());
		}
		
		log.info("--! 测试完毕 ------------------- ");
	}

	
	public static void main(String[] argv) {
		new Provider().call();
		while(true) {
			
		}
	}
}
