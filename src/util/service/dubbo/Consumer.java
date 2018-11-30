package util.service.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import util.service.service.*;

/**
 * Consumer
 *
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"dubbo-consumer.xml"});
        context.start();
        
        ServiceClass s2 = (ServiceClass)context.getBean("serviceClass");
        System.out.println(s2.doClassMethod("util.Pinyin", "getPinYin", "你好噢"));
        
    }
}