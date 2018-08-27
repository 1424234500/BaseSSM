package util.rmi.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import util.rmi.service.ServiceProvider;

public class Provider{
    
    public static void main(String[] args) {
        try {
        	ServiceProvider hello = new ServiceProviderImpl(); /* 生成stub和skeleton,并返回stub代理引用 */
            /* 本地创建并启动RMI Service，被创建的Registry服务将在指定的端口上侦听到来的请求 
             * 实际上，RMI Service本身也是一个RMI应用，我们也可以从远端获取Registry:
             *     public interface Registry extends Remote;
             *     public static Registry getRegistry(String host, int port) throws RemoteException;
             */
            LocateRegistry.createRegistry(9001);
            /* 将stub代理绑定到Registry服务的URL上 */
            Naming.rebind("rmi://localhost:9001/hello", hello);

            System.out.print("Ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
