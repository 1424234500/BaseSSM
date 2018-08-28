package util.dubbo.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import util.dubbo.provider.ServiceProviderImpl;

/**
 * Consumer
 *
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"dubbo-consumer.xml"});
        context.start();
        // Obtaining a remote service proxy
        ServiceProviderImpl demoService = (ServiceProviderImpl)context.getBean("serviceProvider");
        // Executing remote methods
        String hello = demoService.sayHello("world");
        // Display the call result
        System.out.println(hello);
    }
}