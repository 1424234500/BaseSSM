package util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.tools.Tool;

import com.controller.Page;

import util.cache.Cache;
import util.cache.CacheMapImpl;

/**
 * 
 * 类控制 反转 缓存<?类加载机制自带缓存>
 *
 */
public class ClassUtil {
	public static Cache<String> cache = new CacheMapImpl();
	final static String CACHE_KEY = "class-load-cache";

	/**
	 * 初始化目标对象类
	 * @param clsName
	 * @return
	 */
	public static Object newInstance(String clsName) {
		try {
			return loadClass(clsName).newInstance();
		} catch (Exception arg1) {
			throw new RuntimeException(arg1.getMessage(), arg1);
		}
	}

	private static Class<?> loadClass(String className) {
//		Bean bean = cache.get(CACHE_KEY);
//		Class cls = (Class)bean.get(className, null);
		Class<?> cls = null;
		if (cls != null) {
			return cls;
		} else {
			try {
				cls = Thread.currentThread().getContextClassLoader().loadClass(className);
//				bean.put(className, cls);
				return cls;
			} catch (ClassNotFoundException arg4) {
				try {
					return Class.forName(className);
				} catch (Exception arg3) {
					throw new RuntimeException(arg3.getMessage(), arg3);
				} finally{
//					cache.put(CACHE_KEY, bean);
				}
			}finally{
//				cache.put(CACHE_KEY, bean);
			}
		}

	}

	/**
	 * 调用 目标类 的 目标方法 动态参数 返回值
	 * @param className
	 * @param methodName
	 * @param objs
	 * @return
	 */
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
				int len = method.getParameterTypes().length;
				if(len != objs.length){//方法参数 和 传入参数不同  多 或者 少 
					List<Object> args = new ArrayList<>();
					int i = 0;
					for(; i < objs.length && i < len; i++){ //多了 就截取
						args.add(objs[i]);
					}
					for(; i < len; i++){ //少了  填充null
						args.add(null);
					}
					return method.invoke(newClass, args.toArray());
				}else{
					return method.invoke(newClass, objs);
				}
			} catch (Exception arg6) {
				throw new RuntimeException("执行方法[" + cls.getName() + "." + mtdName + "]错误", arg6);
			}
		}else{
			throw new RuntimeException("执行方法[" + cls.getName() + "." + mtdName + "] 不存在");
		}

	}

    /** 
     * 获取某包下所有类 
     * @param packageName 包名 
     * @param childPackage 是否遍历子包 
     * @return new Bean(FILE info, PACKAGE-类的完整名称)
     */  
    public static List<Bean> getPackage(String packageName, boolean childPackage) {  
        List<Bean> fileNames = new ArrayList<>();  
        ClassLoader loader = Thread.currentThread().getContextClassLoader();  
        String packagePath = packageName.replace(".", "/");  
        //只能拿到本项目下的class文件/jar文件
        URL url = loader.getResource(packagePath); 
        //jar:file:/E:/workspace_my/BaseSSM/WebContent/WEB-INF/lib/dom4j-1.6.1.jar!/org/dom4j
        //file:/E:/workspace_my/BaseSSM/WebContent/WEB-INF/classes/util
        if (url != null) {  
            String type = url.getProtocol();  
            if (type.equals("file")) {  
                fileNames = getClassNameByFile(url.getPath(), null, childPackage);  
            } else if (type.equals("jar")) {  
                fileNames = getClassNameByJar(url.getPath(), childPackage);  
            }  
        } else {  
//            fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage);  
        }  
        return fileNames;  
    }  
  
    /** 
     * 从项目文件获取某包下所有类 文件名
     * @param filePath 文件路径 
     * @param className 类名集合 
     * @param childPackage 是否遍历子包 
     * @return 类的完整名称 
     */  
    private static List<Bean> getClassNameByFile(String filePath, List<Bean> className, boolean childPackage) {  
        List<Bean> myClassName = new ArrayList<>();  
        File file = new File(filePath);  
        File[] childFiles = file.listFiles();  
        for (File childFile : childFiles) {  
            if (childFile.isDirectory()) {  
                if (childPackage) {  
                    myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage));  
                }  
            } else {  
                String childFilePath = childFile.getPath();  
                if (childFilePath.endsWith(".class")) {  
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));  
                    childFilePath = childFilePath.replace("\\", ".");  
                    myClassName.add(FileUtil.fileToMap(childFile).put("PACKAGE", childFilePath));  
                }  
            }  
        }  
  
        return myClassName;  
    }  
  
    /** 
     * 从jar获取某包下所有类 
     * @param jarPath jar文件路径          //jar:file:/E:/workspace_my/BaseSSM/WebContent/WEB-INF/lib/dom4j-1.6.1.jar!/org/dom4j
     * @param childPackage 是否遍历子包 
     * @return 类的完整名称 
     */  
    private static List<Bean> getClassNameByJar(String jarPath, boolean childPackage) {  
        List<Bean> myClassName = new ArrayList<>();  
        String[] jarInfo = jarPath.split("!");  
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));  
        String packagePath = jarInfo[1].substring(1);  
        try {  
            @SuppressWarnings("resource")
			JarFile jarFile = new JarFile(jarFilePath);  
            Enumeration<JarEntry> entrys = jarFile.entries();  
            while (entrys.hasMoreElements()) {  
                JarEntry jarEntry = entrys.nextElement();  
                String entryName = jarEntry.getName();  
                if (entryName.endsWith(".class")) {  
                    if (childPackage) {  
                        if (entryName.startsWith(packagePath)) {  
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));  
                            myClassName.add(new Bean().put("PACKAGE", entryName));  
                        }  
                    } else {  
                        int index = entryName.lastIndexOf("/");  
                        String myPackagePath;  
                        if (index != -1) {  
                            myPackagePath = entryName.substring(0, index);  
                        } else {  
                            myPackagePath = entryName;  
                        }  
                        if (myPackagePath.equals(packagePath)) {  
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));  
                            myClassName.add(new Bean().put("PACKAGE", entryName));  
                        }  
                    }  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return myClassName;  
    }  
  
	public static List<Bean> getMethod(String className){
		return getMethod(className, true, false);
	}
    /**
     * 获取包文件列表 
     * 类路径 util.Bean
     * 是否显示成员变量
     * 是否包括父级
     * return new Map(list,size)  <NAME,RETURNTYPE,PARAMETERTYPES,TOSTRING,BASE:0/1,TYPE:method/field>
     */
	public static List<Bean> getMethod(String className, boolean ifArgs, boolean ifFather){
		Class<?> cls = loadClass(className);
		List<Bean> res = new ArrayList<>();
		

		List<Method> methodAll = new ArrayList<>();//所有方法
		Collections.addAll(methodAll, cls.getMethods());

		List<Method> methodSelf = new ArrayList<>();//自有方法
		Collections.addAll(methodAll, cls.getDeclaredMethods());

		methodAll.removeAll(methodSelf); //余下的是基类的方法
		
		if(cls != null){
			if(ifArgs){//如果要变量 则先附加变量
				if(ifFather){ //显示父类 以及自己 的 所有方法 不要变量
					for(Field item : cls.getFields()){
						Bean bean = turnField(className, item);
						res.add(bean);
					}
				}else{//只显示自己的
					for(Field item : cls.getDeclaredFields()){
						Bean bean = turnField(className, item);
						res.add(bean);
					}
				}
			} 
			if(ifFather){ //显示父类 以及自己 的 所有方法 不要变量
				for(Method item : cls.getMethods()){
					Bean bean = turnMethod(className, item);
					res.add(bean);
				}
			}else{//只显示自己的
				for(Method item : cls.getDeclaredMethods()){
					Bean bean = turnMethod(className, item);
					res.add(bean);
				}
			}
		}
		SortUtil.sort(res, false, "BASE");
		
		return res;
	}
	private static Bean turnMethod(String className, Method item){
		Bean bean = new Bean();
		//int aaa = 0;
		bean.put("NAME", item.getName()); //aaa
		bean.put("RETURNTYPE", item.getReturnType());//int
		bean.put("PARAMETERTYPES", Arrays.toString(item.getParameterTypes())
				.replaceAll(className+".", "")
				.replaceAll("java.lang.", ""));
		bean.put("TOSTRING", item.toString()
				.replaceAll(className + ".", "")
				.replaceAll("java.lang.", ""));
		bean.put("TYPE", "method");
		bean.put("BASE", item.getDeclaringClass().getName().equals(className)?"self":"base");
		return bean;
	}
	private static Bean turnField(String className, Field item){
		Bean bean = new Bean();
		//int aaa = 0;
		bean.put("NAME", item.getName()); //aaa
		bean.put("RETURNTYPE", item.getType());//int
		bean.put("PARAMETERTYPES", item.getType());
		bean.put("TOSTRING", item.toString()
				.replaceAll(className + ".", "")
				.replaceAll("java.lang.", ""));
		bean.put("TYPE", "field");
		bean.put("BASE", item.getDeclaringClass().getName().equals(className)?"self":"base");
		return bean;
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
		
		Tools.formatOut(ClassUtil.getPackage("", true));
		Tools.formatOut(ClassUtil.getMethod("com.mode.User"));
		
//		Tools.formatOut(ClassUtil.getClassName("util", true));
//		Tools.formatOut(ClassUtil.getClassName("org.dom4j", true));
	}
}