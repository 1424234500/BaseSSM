package util.service.dubbo;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import util.service.service.*;

/**
 * Consumer
 *
 */
public class Consumer {
//	@Autowired
//    private ServiceClass ss;
	
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
        		new String[] {"dubbo-consumer.xml"});
        context.start();
        new Consumer(context);
    }
    Consumer(ClassPathXmlApplicationContext context) throws RemoteException{

    	ServiceDubbo service = (ServiceDubbo)context.getBean("serviceDubbo");
        System.out.println(service.sayHello("args[]"));
        
    }
}