package com.walker.core.service.serviceImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.jws.WebService;

import com.walker.common.util.ClassUtil;
import com.walker.core.service.service.ServiceClass;

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
@WebService
public class ServiceClassImpl  extends UnicastRemoteObject implements ServiceClass {
	
	private static final long serialVersionUID = 1L;

	public ServiceClassImpl() throws RemoteException{
		super();
	}

	/**
	 * 做一个白名单过滤 依赖缓存的数据  实时更新缓存 
	 * 开放接口 com.service.impl.StudentServiceImplHibernate.list
	 */
	@Override
	public Object doClassMethod(String className, String methodName, Object... methodArgs) {
//		String key = className + "." + methodName;
//		key = key.replace('.', '_');
//		Map<String, Object> map = CacheMgr.getInstance().get("REFLECT_MAP");
//		if(map == null || map.get(key) != null){
			return ClassUtil.doClassMethod(className, methodName, methodArgs);
//		}else{
//			return null;
//		}
		
	}

	@Override
	public String test(String str) throws RemoteException {
		return "echo." + str;
	}
}
