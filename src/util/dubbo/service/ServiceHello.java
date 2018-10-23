package util.dubbo.service;

/**
 * service interface 需要暴露给consumer
 *
 */
public interface ServiceHello {
	public String sayHello(String name);
}
