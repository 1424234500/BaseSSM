package util.dubbo.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import util.dubbo.service.*;

/**
 * Consumer
 *
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"dubbo-consumer.xml"});
        context.start();
        // Obtaining a remote service proxy
        ServiceHello demoService = (ServiceHello)context.getBean("serviceProvider");
        // Executing remote methods
        String hello = demoService.sayHello("world");
        // Display the call result
        System.out.println(hello);
        
        ServiceClass s2 = (ServiceClass)context.getBean("serviceClass");
        System.out.println(s2.doClassMethod("util.Pinyin", "getPinYin", "你好噢"));
        
    }
}