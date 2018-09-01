package util;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
	 * 加载类
	 * @param className
	 */
	public static Class<?> loadClass(String className) {
		Class<?> cls = null;
		try {
			cls = Thread.currentThread().getContextClassLoader().loadClass(className);
		} catch (ClassNotFoundException tcne) {
			try {
				cls = Class.forName(className);
			} catch (ClassNotFoundException cne) {
				out("反射类", className, "异常", cne.toString(), cne.getCause());
				cne.printStackTrace();
			}
		}
		return cls;
	}
	/**
	 * 实例化 类
	 * @param cls
	 */
	private static Object newInstance(Class<?> cls, Object...constructorArgs) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		Class<?>[] args = new Class[constructorArgs.length];
		for (int i = 0; i < constructorArgs.length; ++i) {
			args[i] = constructorArgs[i].getClass();
		}
		Object res = null;
//		res = cls.newInstance(); //调用默认空构造<自动添加/手动编写>(不能是private!)

		Constructor<?> constructor = cls.getDeclaredConstructor(args);
		constructor.setAccessible(true);
		res = constructor.newInstance(constructorArgs);
		return res;
	}
	/**
	 * 实例化方法
	 * @param cls
	 * @param methodName
	 */
	private static Method newMethod(Class<?> cls, String methodName, Object...methodArgs) throws NoSuchMethodException, SecurityException{
		Class<?>[] args = new Class[methodArgs.length];
		for (int i = 0; i < methodArgs.length; ++i) {
			args[i] = methodArgs[i].getClass();
		}
		Method method = null;
		try{
			method = cls.getMethod(methodName, args); //查询非自己private函数
		}catch(NoSuchMethodException noPrivateNsme){ 
//			out("反射 非私有private函数域没有这个方法 即将查找private区域  " + noPrivateNsme.toString());
			method = cls.getDeclaredMethod(methodName, args); //查询自己private函数
		}
		return method;
	}
	/**
	 * 调用默认构造实例化的 目标方法 目标参数
	 * @param className
	 * @param methodName
	 * @param objs
	 * @return
	 */
	public static Object doClassMethod(String className, String methodName, Object... methodArgs) {
		return doClassMethod(loadClass(className), new Object[]{}, methodName, methodArgs);
	}
	/**
	 * 调用特定构造实例化的 目标方法 目标参数
	 * @param className
	 * @param constructorArgs
	 * @param methodName
	 * @param methodArgs
	 * @return
	 */
	public static Object doClassMethod(String className, Object[] constructorArgs, String methodName, Object... methodArgs) {
		return doClassMethod(loadClass(className), constructorArgs, methodName, methodArgs);
	}
	public static Object doClassMethod(Class<?> cls, String methodName, Object... methodArgs) {
		return doClassMethod(cls, new Object[]{}, methodName, methodArgs);
	}
	public static Object doClassMethod(Class<?> cls, Method method, Object... methodArgs) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		return method.invoke(newInstance(cls), methodArgs);
	}
	public static Object doClassMethod(Class<?> cls, Object[] constructorArgs, String methodName, Object... methodArgs) {
		Object res = null;

		try {
			Method method = newMethod(cls, methodName, methodArgs);
			method.setAccessible(true);
			Object instance = newInstance(cls, constructorArgs);
			res = method.invoke(instance, methodArgs);
		} catch(NoSuchMethodException nsme){
			out("反射[" + cls.getName() + "." + methodName + "]" + Arrays.toString(methodArgs) + " 没有这个方法 " + nsme.toString());
		} catch (InstantiationException ie) {
			out("反射[" + cls.getName() + "." + methodName + "]" + Arrays.toString(methodArgs) + " 类实例化异常 " + ie.toString());
		} catch (IllegalAccessException iae) {
			out("反射[" + cls.getName() + "." + methodName + "]" + Arrays.toString(methodArgs) + " 函数参数违法 " + iae.toString());
		} catch (InvocationTargetException iaee){
			out("反射[" + cls.getName() + "." + methodName + "]" + Arrays.toString(methodArgs) + " 唤醒对象异常 " + iaee.toString());
		} catch (Exception e){
			out("反射[" + cls.getName() + "." + methodName + "]" + Arrays.toString(methodArgs) + " 其他异常 " + e.toString());
		} finally {
			
		}
		return res;
	}
	private static void out(Object...objects){
		Tools.out(objects);
	}
	private final static String defaultType = "String";
	/**
	 * 根据规则string获取对象列表
	 * @param args     : String-sss@Bean-{"k":"v"}@Integer-111@Boolean-true
	 * @param splitArr : @
	 * @param splitArg : -
	 * return [String(sss), Bean(k,v), Integer(111), Boolean(true)
	 */
	public static Object[] parseObject(String args, String splitArr, String splitArg){
		List<Object> res = new ArrayList<>();
		
		if(splitArr == null || splitArr.length() == 0){
			res.add(parseObject(defaultType, args));
		}else if(args == null){
			res.add(null);
		}else{
			String[] arr = args.split(splitArr);
			if(splitArg == null || splitArg.length() == 0){
				for(String item : arr){
					res.add(parseObject(defaultType, item));
				}
			}else{
				for(String item : arr){
					String[] typeValue = item.split(splitArg);
					if(typeValue.length > 1){
						res.add(parseObject(typeValue[0], typeValue[1]));
					}else{
						res.add(parseObject(defaultType, item));
					}
				}
			}
		}
		
		return res.toArray();
	}
	public static Object parseObject(String type, String value){
		Object res = null;
		if(type == null || type.length() == 0){
			type = defaultType;
		}
		type = type.toLowerCase();
		if(value != null){
			try{
				if(type.equals("string")){
					res = new String(value);
				}else if(type.equals("int") || type.equals("integer")){
					res = new Integer(value);
				}else if(type.equals("long")){
					res = new Long(value);
				}else if(type.equals("double")){
					res = new Double(value);
				}else if(type.equals("float")){
					res = new Float(value);
				}else if(type.equals("boolean") || type.equals("bool")){
					res = new Boolean(value);
				}
				//常用对象类型
				else if(type.equals("hashmap")){ //无法实例化原生Map 
					res = new HashMap<Object, Object>(JsonUtil.getMap(value));
				}else if(type.equals("arraylist")){ //无法实例化原生List
					res = new ArrayList<Object>(JsonUtil.getList(value));
				}else if(type.equals("bean")){
					res = new Bean(JsonUtil.getMap(value));
				}
				//序列化传输 必须使用序列化 完整的str byte[]的数据
				else if(type.equals("seria") || type.equals("serializeUtil")){
					res = SerializeUtil.deserialize(util.encode.Base64.decode(value));
				}
			
			}catch(Exception e){
				e.printStackTrace();
				res = null;
			}
		}
		
		return res;		
	}
	
	
    /** 
     * 获取某包下所有类 及其文件信息
     * @param packageName 包名 
     * @param childPackage 是否遍历子包 
     * @return new Bean(FILE info, PACKAGE-类的完整名称)
     */  
    public static List<Bean> getPackageClassBean(String packageName, boolean childPackage) {  
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
                fileNames = getClassBeanByFile(url.getPath(), childPackage);  
            } else if (type.equals("jar")) {  
                fileNames = getClassBeanByJar(url.getPath(), childPackage);  
            }  
        } else {  
//            fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage);  
        }  
        return fileNames;  
    }  
    public static List<String> getPackageClass(String packageName, boolean childPackage) {  
        List<String> fileNames = new ArrayList<>();  
        ClassLoader loader = Thread.currentThread().getContextClassLoader();  
        String packagePath = packageName.replace(".", "/");  
        //只能拿到本项目下的class文件/jar文件
        URL url = loader.getResource(packagePath); 
        //jar:file:/E:/workspace_my/BaseSSM/WebContent/WEB-INF/lib/dom4j-1.6.1.jar!/org/dom4j
        //file:/E:/workspace_my/BaseSSM/WebContent/WEB-INF/classes/util
        if (url != null) {  
            String type = url.getProtocol();  
            if (type.equals("file")) {  
                fileNames = getClassByFile(url.getPath(), childPackage);  
            } else if (type.equals("jar")) {  
//                fileNames = getClassBeanByJar(url.getPath(), childPackage);  
            }  
        } else {  
//            fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage);  
        }  
        return fileNames;  
    }  
    /** 
     * 从项目文件获取某包下所有类 
     * @param filePath 文件路径 
     * @param childPackage 是否遍历子包 
     * @return 类的完整名称 及其 文件信息
     */  
    private static List<Bean> getClassBeanByFile(String filePath, boolean childPackage) {  
        List<Bean> myClassName = new ArrayList<>();  
        File file = new File(filePath);  
        File[] childFiles = file.listFiles();  
        for (File childFile : childFiles) {  
            if (childFile.isDirectory()) {  
                if (childPackage) {  
                    myClassName.addAll(getClassBeanByFile(childFile.getPath(), childPackage));  
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
     * 从项目文件获取某包下所有class 类全路径
     * @param filePath 文件路径 
     * @param childPackage 是否遍历子包 
     * @return 类的完整名称 
     */  
    private static List<String> getClassByFile(String filePath, boolean childPackage) {  
        List<String> myClassName = new ArrayList<>();  
        File file = new File(filePath);  
        File[] childFiles = file.listFiles();  
        for (File childFile : childFiles) {  
            if (childFile.isDirectory()) {  
                if (childPackage) {  
                    myClassName.addAll(getClassByFile(childFile.getPath(), childPackage));  
                }  
            } else {  
                String childFilePath = childFile.getPath();  
                if (childFilePath.endsWith(".class")) {  
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));  
                    childFilePath = childFilePath.replace("\\", ".");  
                    myClassName.add(childFilePath);  
                }  
            }  
        }  
  
        return myClassName;  
    }  
  
    /** 
     * 从jar获取某包下所有类 
     * @param jarPath jar文件路径          //jar:file:/E:/workspace_my/BaseSSM/WebContent/WEB-INF/lib/dom4j-1.6.1.jar!/org/dom4j
     * @param childPackage 是否遍历子包 
     * @return 类的完整名称 及其 文件信息
     */  
    private static List<Bean> getClassBeanByJar(String jarPath, boolean childPackage) {  
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
					//附加自己私有域
					for(Field item : cls.getDeclaredFields()){
						if(getModifier(item).indexOf("private") >= 0){
							res.add(turnField(className, item));
						}
					}
				}else{//只显示自己的
					for(Field item : cls.getDeclaredFields()){
						Bean bean = turnField(className, item);
						res.add(bean);
					}
				}
			} 
			if(ifFather){ //显示父类(仅public) 以及自己 的(不包括private)所有方法 不要变量 
				for(Method item : cls.getMethods()){
					Bean bean = turnMethod(className, item);
					res.add(bean);
				}
				//附加自己私有域
				for(Method item : cls.getDeclaredMethods()){
					if(getModifier(item).indexOf("private") >= 0){
						res.add(turnMethod(className, item));
					}
				}
			}else{//只显示自己的(所有的 包括private)
				for(Method item : cls.getDeclaredMethods()){
					Bean bean = turnMethod(className, item);
					res.add(bean);
				}
			}
		}
		SortUtil.sort(res, false, "BASE");
		
		return res;
	}
	private static String filterString(String className, String str){
		return str
				.replaceAll(className+".", "")
				.replaceAll("java.lang.", "")
				.replaceAll("class ", "")
				.replaceAll("java.util.", "")
				.replaceAll("javax.servlet.http.", "")
				.replaceAll("interface ", "");
	}
	private static Bean turnMethod(String className, Method item){
		Bean bean = new Bean();
		//int aaa = 0;
		bean.put("NAME", item.getName()); //aaa
		bean.put("RETURNTYPE", item.getReturnType());//int
		
		String str = filterString(className, Arrays.toString(item.getParameterTypes()));
		if(str.length() >= 2){//[*, *]
			str = str.substring(1,  str.length() - 1);
		}
		bean.put("PARAMETERTYPES", str.split(", "));
		
		bean.put("TOSTRING", filterString(className, item.toString()));
		bean.put("MODIFIER", getModifier(item));//public static
		bean.put("TYPE", "method");
		bean.put("BASE", item.getDeclaringClass().getName().equals(className)?"self":"base");
		return bean;
	}
	/**
	 * 获取Method 或者Field 的修饰符 public static final
	 * @param member
	 * @return
	 */
	private static String getModifier(Member member){
		int mod = member.getModifiers() & Modifier.methodModifiers();
        if (mod != 0) {
            return Modifier.toString(mod);
        }
        return "";
	}
	private static Bean turnField(String className, Field item){
		Bean bean = new Bean();
		//int aaa = 0;
		bean.put("NAME", item.getName()); //aaa
		bean.put("RETURNTYPE", item.getType());//int
		bean.put("PARAMETERTYPES", item.getType());
		bean.put("TOSTRING", filterString(className, item.toString()));
		bean.put("TYPE", "field");
		bean.put("MODIFIER", getModifier(item));//public static
		bean.put("BASE", item.getDeclaringClass().getName().equals(className)?"self":"base");
		return bean;
	}
    
    
    
	private ClassUtil(){
		out("private constructor");
	}
	private ClassUtil(String str){
		out("private constructor str");
	}
	private ClassUtil(String str, Integer i){
		out("private constructor str integer");
	}
	

	public String test(String str, Bean bean, Integer in, Boolean bool){
		return Arrays.toString(new Object[]{str, bean, in, bool});
	}
	public String testNoArgs(){
		out();
		return "testNoArgs";
	}
	//int 不行?
	public String testInt(int i){
		out(i);
		return "testInt";
	}
	public String testInt(Integer i){
		out(i);
		return "testIntger";
	}
	public String testStr(String i){
		out(i);
		return "testStr";
	}
	private String testBean(Bean i){
		out(i);
		return "testBean";
	}
	//需要指定实际map类型
	public String testMap(HashMap<?,?> i){
		out(i);
		return "testMap";
	}
	public String testObjects(Object... objs){
		out(objs.getClass());
		out(objs.length);
		out(objs);
		return "testObjects";
	}
	public void testNoReturn(){
		out("noReturn");
	}
	
	public <T> T[] testCreate(T type){
		return (T[]) Array.newInstance(type.getClass(), 10);
	}

	public <T> T[] testCreate(Class<T> cls){
		return (T[]) Array.newInstance(cls, 10);
	}
	//推断 协变类型  下转
	public <T> T[] testCreate1(Class<?> cls){
		return (T[]) Array.newInstance(cls, 10);
	}
	public static void main(String argc[]){
		out(ClassUtil.doClassMethod("util.ClassUtil", "testNoArgs"));
		out(ClassUtil.doClassMethod("util.ClassUtil", new Object[]{}, "testInt", 1));
		out(ClassUtil.doClassMethod("util.ClassUtil", new Object[]{}, "testIntger", 1));
		out(ClassUtil.doClassMethod("util.ClassUtil", new Object[]{"str"}, "testStr", "str"));
		out(ClassUtil.doClassMethod("util.ClassUtil", new Object[]{"str", 111}, "testBean", new Bean().put("key", "vvv")));
//		out(ClassUtil.doClassMethod("util.ClassUtil", "testObjects", new String[]{"str", "str2"}));
		out(ClassUtil.doClassMethod("util.ClassUtil", "testNoReturn"));
		
		out(ClassUtil.doClassMethod("util.ClassUtil", "test", ClassUtil.parseObject("String-sss@Bean-{\"k\":\"v\"}@Integer-111@Boolean-true", "@", "-")));
		byte[] bb = SerializeUtil.serialize(new Bean().set("key", "value").set("key2", "value2"));
		String strSeria = new String(util.encode.Base64.encode(bb));
		byte[] bt = util.encode.Base64.decode(strSeria);
		out("seria", bb, strSeria);
		out("dseria byte", SerializeUtil.deserialize(bb));
		out("dseria str ", SerializeUtil.deserialize(bt));
		out(ClassUtil.doClassMethod("util.ClassUtil", "test", ClassUtil.parseObject("String-sss@seria-"
				+ strSeria + 
				"@Integer-111@Boolean-true", "@", "-")));

		
		Tools.formatOut(ClassUtil.getPackageClassBean("", true));
//		Tools.formatOut(ClassUtil.getMethod("com.mode.User"));
		
//		Tools.formatOut(ClassUtil.getClassName("util", true));
//		Tools.formatOut(ClassUtil.getClassName("org.dom4j", true));
	}
}