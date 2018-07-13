package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.tools.Tool;

/**
 * 
 * 类控制 反转 缓存<?类加载机制自带缓存>
 *
 */
public class ClassUtil {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<String, Class> clsMap = new ConcurrentHashMap<String, Class>();

	public static Object newInstance(String clsName) {
		try {
			return loadClass(clsName).newInstance();
		} catch (Exception arg1) {
			throw new RuntimeException(arg1.getMessage(), arg1);
		}
	}

	private static Class loadClass(String className) {
		Class cls = clsMap.get(className);
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

	public static Object doClassMethod(String className, String methodName, Object... objs) {
		return doClassMethod(loadClass(className), methodName, objs);
	}

	public static Object doClassMethod(Class<?> cls, String mtdName, Object... objs) {
		Method method = null;
		Object newClass = null;
		try {
			Class<?>[] e = new Class[objs.length];
			for (int i = 0; i < objs.length; ++i) {
				e[i] = objs[i].getClass();
			}
			method = cls.getMethod(mtdName, e);
		} catch (Exception arg7) {
			arg7.printStackTrace();
		}
		if (method != null) {
			try {
				newClass = cls.newInstance();
				return method.invoke(newClass, objs);
			} catch (Exception arg6) {
				throw new RuntimeException("执行方法[" + cls.getName() + "." + mtdName + "]错误", arg6);
			}
		}else{
			throw new RuntimeException("执行方法[" + cls.getName() + "." + mtdName + "] 不存在");
		}

	}
	private static void out(Object... objs) {
		Tools.out(objs);
	}

	public String testNoArgs(){
		Tools.out();
		return "testNoArgs";
	}
	//int 不行?
	public String testInt(Integer i){
		Tools.out(i);
		return "testInt";
	}
	public String testStr(String i){
		Tools.out(i);
		return "testStr";
	}
	//需要指定实际map类型
	public String testMap(HashMap<?,?> i){
		Tools.out(i);
		return "testMap";
	}
	public String testObjects(Object... objs){
		Tools.out(objs.getClass());
		Tools.out(objs.length);
		Tools.out(objs);
		return "testObjects";
	}
	public void testNoReturn(){
		Tools.out("noReturn");
	}
	
	public static void main(String argc[]){
		new ClassUtil().testObjects(new String[]{"1","2"});
		new ClassUtil().testObjects("1","2");
		new ClassUtil().testNoReturn();
		Tools.out(ClassUtil.doClassMethod("util.ClassUtil", "testNoArgs"));
		Tools.out(ClassUtil.doClassMethod("util.ClassUtil", "testInt", 1));
		Tools.out(ClassUtil.doClassMethod("util.ClassUtil", "testStr", "str"));
		Tools.out(ClassUtil.doClassMethod("util.ClassUtil", "testMap", MapListUtil.getMap().put("key", "vvv").build()));
//		Tools.out(ClassUtil.doClassMethod("util.ClassUtil", "testObjects", new String[]{"str", "str2"}));
		Tools.out(ClassUtil.doClassMethod("util.ClassUtil", "testNoReturn"));
	}
}