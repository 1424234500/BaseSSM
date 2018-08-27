package util.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * service interface 需要暴露给consumer
 *
 */
public interface ServiceProvider extends Remote {
	/**
	 * 接口占位异常抛出 rmi需要
	 * @param name
	 * @return
	 * @throws RemoteException
	 */
	public String sayHello(String name) throws RemoteException; 
}
