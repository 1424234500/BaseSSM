package util.rmi.client;

import java.rmi.Naming;

import util.rmi.service.ServiceProvider;

/**
 * Consumer
 *
 */
public class Consumer {

    public static void main(String[] args) {
        try {
            /* 从RMI Registry中请求stub
             * 如果RMI Service就在本地机器上，URL就是：rmi://localhost:1099/hello
             * 否则，URL就是：rmi://RMIService_IP:1099/hello
             */
        	ServiceProvider hello = (ServiceProvider) Naming.lookup("rmi://localhost:9001/hello");
            /* 通过stub调用远程接口实现 */
            System.out.println(hello.sayHello("nnnnn"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}