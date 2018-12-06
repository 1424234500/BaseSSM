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
import util.service.serviceImpl.ServiceClassWebserviceImpl;

/**
 * service provider
 *
 */
public class Provider implements Call{
	private static Logger log = Logger.getLogger(Provider.class); 
	private static Map<String, Object> map;
	static {
		map = new HashMap<>();
	}
	/**
	 * 初始化服务
	 * 验证测试
	 */
	@Override
	public void call() {
		log.info("** 初始化 dubbo provider ---------------------- ");
		
		String port = "8089";
		List<Object> list = new ArrayList<>();
		list.add(new ServiceClassWebserviceImpl());

		System.setProperty("java.net.preferIPv4Stack", "true");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "dubbo-provider.xml" });
		context.start(); 
		log.info("**! 初始化完毕 dubbo provider------------------- ");
		
		log.info("-- 开始测试dubbo --------------");
		try {
//			log.info(HttpUtil.get(map.keySet().iterator().next()));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("test dubbo service error !" + e.toString());
		}
		
		log.info("--! 测试完毕 ------------------- ");
	}

	
	@Test
	public void test(){
		new Provider().call();
		ThreadUtil.sleep(3600 * 1000);
	}
}
