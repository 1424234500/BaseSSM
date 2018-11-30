package util.service.serviceImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import util.service.service.ServiceHelloRmi;


/**
 * service Rmi需要异常
 *
 *
 */
public class ServiceClassRmiImpl extends UnicastRemoteObject implements ServiceHelloRmi{
	private static final long serialVersionUID = 1L;

    public ServiceClassRmiImpl() throws RemoteException {
        super();
    }
    
	public String sayHello(String name) throws RemoteException {
		return "Hello " + name;
	}
    
}