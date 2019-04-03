package com.walker.core.service.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * service interface 需要暴露给consumer rmi特殊需要
 *
 */
public interface ServiceHelloRmi extends Remote {
	/**
	 * 接口占位异常抛出 rmi需要
	 * @param name
	 * @return
	 * @throws RemoteException
	 */
	public String sayHello(String name) throws RemoteException; 
}
