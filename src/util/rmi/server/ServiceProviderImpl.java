package util.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import util.rmi.service.ServiceProvider;

/**
 * service
 *
 */
public class ServiceProviderImpl extends UnicastRemoteObject implements ServiceProvider{
	private static final long serialVersionUID = 1L;

	// 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常  
    protected ServiceProviderImpl() throws RemoteException {
        super();
    }
    
	public String sayHello(String name) throws RemoteException {
		return "Hello " + name;
	}
    
}