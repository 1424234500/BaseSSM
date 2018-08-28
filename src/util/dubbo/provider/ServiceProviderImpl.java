package util.dubbo.provider;

import util.dubbo.service.ServiceProvider;

/**
 * service
 *
 */
public class ServiceProviderImpl implements ServiceProvider {
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
