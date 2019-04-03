package com.walker.core.service.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * service interface 需要暴露给consumer
 * 
 * 通用适配接口
 * 
 * 1.Rmi 需要Remote父类 和 异常抛出RemoteException
 * 
 * 2.WebService不需要接口上转 不需要暴露接口 而是暴露 wsdl 需要注解
 * 
 *
 */
public interface ServiceClass extends Remote {

	/**
	 * test 
	 * 接口占位异常抛出 rmi需要
	 */
	String test(String str) throws RemoteException;
	
	/**
	 * 代理反射方法调用
	 * @param className
	 * @param methodName
	 * @param methodArgs 序列化 rmi webservice会导致 转型为List<Object>
	 * @return
	 */
	Object doClassMethod(String className, String methodName, Object...methodArgs) throws RemoteException; 




}
