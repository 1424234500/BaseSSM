package util.dubbo.provider;

import java.util.List;
import java.util.Map;

import util.ClassUtil;
import util.cache.CacheMgr;
import util.dubbo.service.ServiceClass;

/**
 * service
 *
 */
public class ServiceClassImpl implements ServiceClass {
	
	/**
	 * 做一个白名单过滤 依赖缓存的数据  实时更新缓存 
	 */
	@Override
	public Object doClassMethod(String className, String methodName, Object... methodArgs) {
		String key = className + "." + methodName;
		key = key.replace('.', '_');
		Map<String, Object> map = CacheMgr.getInstance().get("REFLECT_MAP");
		if(map == null || map.get(key) != null){
			return ClassUtil.doClassMethod(className, methodName, methodArgs);
		}else{
			return null;
		}
		
	}
}
