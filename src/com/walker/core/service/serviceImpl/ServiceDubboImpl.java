package com.walker.core.service.serviceImpl;


import com.walker.core.service.service.ServiceDubbo;


/**
 *
 */
public class ServiceDubboImpl implements ServiceDubbo{
    
	public String sayHello(String name) {
		return "Hello dubbo " + name;
	}
    
}