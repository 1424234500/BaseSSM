package util.dubbo.service;

/**
 * service interface 需要暴露给consumer
 *
 */
public interface ServiceProvider {
	public String sayHello(String name);
}
