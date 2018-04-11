package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * 
 * 类控制 反转 缓存
 *
 */
public class ClassUtil {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<String, Class> clsMap = new ConcurrentHashMap();

	public static Object createObject(String clsName) {
		try {
			return loadClass(clsName).newInstance();
		} catch (Exception arg1) {
			throw new RuntimeException(arg1.getMessage(), arg1);
		}
	}

	public static Class loadClass(String className) {
		Class cls = (Class) clsMap.get(className);
		if (cls != null) {
			return cls;

		} else {
			try {
				cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				clsMap.put(className, cls);
				return cls;
			} catch (ClassNotFoundException arg4) {
				try {
					return Class.forName(className);
				} catch (Exception arg3) {
					throw new RuntimeException(arg3.getMessage(), arg3);
				}
			}

		}

	}

	public static void doClassMethod(String className, String methodName, Object... objs) {
		doClassMethod(loadClass(className), methodName, objs);
	}

	public static void doClassMethod(Class<?> cls, String mtdName, Object... objs) {
		Method method = null;
		Object newClass = null;
		try {
			if (objs.length > 0) {

				Class[] e = new Class[objs.length];
				for (int i = 0; i < objs.length; ++i) {
					e[i] = objs[i].getClass();
				}

				method = cls.getMethod(mtdName, e);
			} else {
				method = cls.getMethod(mtdName, new Class[0]);
			}
		} catch (Exception arg7) {
			arg7.printStackTrace();
		}
		if (method != null) {
			try {
				newClass = cls.newInstance();
				if (objs.length > 0) {
					method.invoke(newClass, objs);
				} else {
					method.invoke(newClass, new Object[0]);
				}
			} catch (Exception arg6) {
				if (arg6.getCause() instanceof RuntimeException) {
					throw (RuntimeException) ((RuntimeException) arg6.getCause());
				}
				throw new RuntimeException("执行方法[" + cls.getName() + "." + mtdName + "]错误", arg6);
			}
		}

	}
	public static void out(Object... objs) {
		Tools.out(objs);
	}
	public String test(){
		Tools.out();
		return "11111111111";
	}
	public static void main(String argc[]){
		ClassUtil.doClassMethod("util.ClassUtil", "test", 1);
	}
}