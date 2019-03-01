package util.service.serviceImpl;


import util.service.service.ServiceDubbo;


/**
 *
 */
public class ServiceDubboImpl implements ServiceDubbo{
    
	public String sayHello(String name) {
		return "Hello dubbo " + name;
	}
    
}