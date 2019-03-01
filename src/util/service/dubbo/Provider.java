package util.service.dubbo;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import util.Call;
import util.service.service.ServiceDubbo;

/**
 * service provider
 * 
 * 依赖jar包
 * dubbo-2.5.6.jar
 * netty-3.2.5.Final.jar
 * zkclient-0.1.0.jar
 * zookeepper-3.3.3.jar
 *
 * 配置文件 
 * dubbo-provider.xml <dubbo.properties>
 * 
 */
public class Provider implements Call{
	private static Logger log = Logger.getLogger(Provider.class); 

	/**
	 * 启动dubbo
	 * 独立的容器，因为服务通常不需要Tomcat/JBoss等Web容器的特性，没必要用Web容器去加载服务。
	 * Container容器接口有只有 start()  stop()
	 * java命令-D参数或者dubbo.properties中配置
     * 实现类：
     * SpringContainer、 默认
     * 	 	自动加载META-INF/spring目录下的所有Spring配置。
     * 		dubbo.spring.config=classpath*:META-INF/spring/*.xml
     * Log4jContainer、JettyContainer、JavaConfigContainer、LogbackContainer。
     * 
     * 关闭
     * 通过JDK的ShutdownHook来完成优雅停机的，
     * 使用"kill -9    PID"等强制关闭指令，是不会执行优雅停机
     * 只有通过"kill PID"时，才会执行
	 */
	@Override
	public void call() {
		log.info("** 初始化 dubbo provider ---------------------- ");
		com.alibaba.dubbo.container.Main.main(new String[] {"dubbo-provider.xml"});

//		System.setProperty("java.net.preferIPv4Stack", "true");
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "dubbo-provider.xml" });
//		context.start(); 
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
