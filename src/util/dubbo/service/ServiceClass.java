package util.dubbo.service;

/**
 * service interface 需要暴露给consumer
 *
 */
public interface ServiceClass {
	/**
	 * 代理反射方法调用
	 * @param className
	 * @param methodName
	 * @param objects
	 * @return
	 */
	Object doClassMethod(String className, String methodName, Object...parameters);
}
