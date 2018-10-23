package util.dubbo.provider;

import util.dubbo.service.ServiceHello;

/**
 * service
 *
 */
public class ServiceHelloImpl implements ServiceHello {
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
