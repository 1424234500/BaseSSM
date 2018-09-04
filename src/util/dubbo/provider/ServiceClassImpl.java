package util.dubbo.provider;

import util.ClassUtil;
import util.dubbo.service.ServiceClass;

/**
 * service
 *
 */
public class ServiceClassImpl implements ServiceClass {

	@Override
	public Object doClassMethod(String className, String methodName, Object... methodArgs) {
		return ClassUtil.doClassMethod(className, methodName, methodArgs);
	}
}
