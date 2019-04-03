package com.walker.core.service.serviceImpl;

import java.rmi.RemoteException;

import javax.jws.WebService;

import com.walker.core.service.service.ServiceClass;

/**
 * @WebService 需要注解
 *
 */
@WebService
public class ServiceClassWebserviceImpl implements ServiceClass {
	
	@Override
	public String test(String name) {
		return "echo." + name;
	}
	
	@Override
	public Object doClassMethod(String className, String methodName, Object...methodArgs) throws RemoteException{
		return new ServiceClassImpl().doClassMethod(className, methodName, methodArgs);
	}

}
