package com.walker.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.walker.common.util.ClassUtil;
import com.walker.common.util.Tools;

/**
 * 注解扫描处理器工具 抽离出目标注解(Class, Method, Field)的处理实现 支持处理链? 一次扫描 所有注解全部处理 只处理本项目下的注解
 * 
 */
public class TrackerUtil {
	public static void main(String[] argv){
		Tracker[] trackerClass = {
				new Tracker(DBTable.class, ElementType.TYPE,  new DBTableTracker()),
		};
		Tracker[] trackerField = {
//				new Tracker(DBConstraints.class, ElementType.FIELD,  new DBTableTracker()),
//				new Tracker(DBSQLInteger.class, ElementType.FIELD,  new DBTableTracker()),
//				new Tracker(DBSQLString.class, ElementType.FIELD,  new DBTableTracker()),
//				new Tracker(DBTable.class, ElementType.FIELD,  new DBTableTracker()),
		};
		Tracker[] trackerMethod = {
				new Tracker(UseCase.class, ElementType.METHOD,  new UseCaseTracker()),
				new Tracker(Test.class, ElementType.METHOD,  new TestTracker()),
		};

		Tracker[][] trackerAll = { trackerClass, trackerField, trackerMethod }; //分层
		TrackerUtil.make("", trackerAll);
	}
	
	
	/**
	 * 默认扫描util.annotation包 找出所有的 注解定义 和 注解处理器  快捷初始化tracker
	 */
	public static Tracker[][] scan(String clsPackage){
		EnumMap<ElementType, Set<Tracker>> map = new EnumMap<>(ElementType.class);
		map.put(ElementType.TYPE, new HashSet<Tracker>());
		map.put(ElementType.METHOD, new HashSet<Tracker>());
		map.put(ElementType.FIELD, new HashSet<Tracker>());
		
		clsPackage = clsPackage == null || clsPackage.length() == 0 ? "util.annotation" : clsPackage;
		List<String> classs = ClassUtil.getPackageClass(clsPackage, false);
		for (String item : classs) { // 按照 包名 类 深度(class->field->method)优先 处理
//			Tools.out(item);
			Class<?> cls = ClassUtil.loadClass(item);
			if (cls == null)
				continue;
			//取出这两个注解修饰  判定是否是一个 注解定义
			//@Target(ElementType.TYPE)
			//@Retention(RetentionPolicy.RUNTIME)
			Annotation targetAnno = cls.getAnnotation(Target.class); 
			if (targetAnno != null){
				Target target = (Target)targetAnno;
				ElementType[] et = target.value(); //Field Method Type
				String clsName = cls.getName(); 				//util.annotation.DBTable
				String trackerClsName = clsName + "Tracker";	//util.annotation.DBTableTracker
				try{
					Object makerObj = ClassUtil.newInstance(trackerClsName);
					//new Tracker(UseCase.class, ElementType.METHOD,  new UseCaseTracker()),
					if(makerObj != null){
						OnAnnotation maker = (OnAnnotation) makerObj;
						for(ElementType type : et){
							map.get(type).add(new Tracker((Class<? extends Annotation>) cls, type, maker));
						}
					}
				}catch(Exception e){
					Tools.out(" annotation scan error: " + clsName + " 's tracker error " + e.toString());
				}
			}
			
			
		}
		
		return new Tracker[][]{
			map.get(ElementType.TYPE).toArray(new Tracker[0]), 
			map.get(ElementType.METHOD).toArray(new Tracker[0]),
			map.get(ElementType.FIELD).toArray(new Tracker[0]), 
		};
	}
	
	/**
	 * 把注解对应的处理器调用处理 分为三级层次 
	 * @param clsPackage
	 * @param trackerClass
	 * @param trackerField
	 * @param trackerMethod
	 */
	public static void make(String clsPackage, Tracker[][] trackerAll) {
		// 获取所有class文件列表 可配置包名 来限定扫描控制
		List<String> classs = ClassUtil.getPackageClass("", true);
		
		
		// 判断class包含注解列表 匹配 待处理列表
		Tracker[] trackerType = null;
		Tracker tracker = null;
		Status status = Status.NORMAL;
		int i = 0, type = 0;
		for (String item : classs) { // 按照 包名 类 深度(class->field->method)优先 处理
//			Tools.out(item);
			Class<?> cls = ClassUtil.loadClass(item);
			if (cls == null)
				continue;
			
			for (type = trackerAll.length - 1; type >= 0; type--) {
				trackerType = trackerAll[type];
				
				// 1.class级别注解
				// 2.field级别注解
				// 3.method级别注解
				for (i = 0; i < trackerType.length; i++) {
					tracker = trackerType[i];
					if(tracker == null) continue;
					
					//目标->Anno->目标 传递! eg:处理Test注解
					//判定是 class 级别或者 field 或者 method
					if(ElementType.TYPE.equals(tracker.type)){
						status = doCall(cls, cls, tracker);
					}else if(ElementType.FIELD.equals(tracker.type)){
						for(Field field : cls.getDeclaredFields()){
							status = doCall(cls, field, tracker);
						}
					}else if(ElementType.METHOD.equals(tracker.type)){
						for(Method method : cls.getDeclaredMethods()){
							status = doCall(cls, method, tracker);
						}
					}else{
						continue;
					}

					if(status == Status.NORMAL){
						continue;
					}else if(status == Status.STOP_CLASS){ //
						continue;
					}else if(status == Status.STOP_NAME){
						break;
					}else if(status == Status.STOP){
						break;
					}
					//一次类 - 注解 - 域 执行完毕
				}
				//所有类 的 该级别执行完毕
				if(status == Status.NORMAL){
					continue;
				}else if(status == Status.STOP_CLASS){ //
					break;
				}else if(status == Status.STOP_NAME){
					continue;
				}else if(status == Status.STOP){
					break;
				}
				//一次类 - 注解 - 所有域 执行完毕
			}
			//所有类执行完毕
		}
		
	}
	private static Status doCall(Class<?> cls, Class<?> elem, Tracker tracker){
		Annotation anno = elem.getAnnotation(tracker.cls); 
		if (anno != null && tracker.fun != null){
			Status res = tracker.fun.make(anno, tracker.type, elem, cls); // 命中注解 且 有处理器 则处理 并获取处理回调值
			res = res == null ? Status.NORMAL : res;
			return res;

		}
		return Status.NORMAL;
	}
	private static Status doCall(Class<?> cls, Field elem, Tracker tracker){
		Annotation anno = elem.getAnnotation(tracker.cls); 
		if (anno != null && tracker.fun != null){
			Status res = tracker.fun.make(anno, tracker.type, elem, cls); // 命中注解 且 有处理器 则处理 并获取处理回调值
			res = res == null ? Status.NORMAL : res;
			return res;
		}
		return Status.NORMAL;
	}
	private static Status doCall(Class<?> cls, Method elem, Tracker tracker){
		Annotation anno = elem.getAnnotation(tracker.cls); 
		if (anno != null && tracker.fun != null){
			Status res = tracker.fun.make(anno, tracker.type, elem, cls); // 命中注解 且 有处理器 则处理 并获取处理回调值
			res = res == null ? Status.NORMAL : res;
			return res;
		}
		return Status.NORMAL;
	}
	private static Status doCall(Class<?> cls, AnnotatedElement elem, Tracker tracker){
		Annotation anno = elem.getAnnotation(tracker.cls); 
		if (anno != null && tracker.fun != null){
			Status res = tracker.fun.make(anno, tracker.type, elem, cls); // 命中注解 且 有处理器 则处理 并获取处理回调值
			res = res == null ? Status.NORMAL : res;
			return res;
		}
		return Status.NORMAL;
	}

}

/**
 * 处理结果 决定该类的其他处理器是否执行
 */
enum Status {
	/**
	 * 正常模式 处理完com.ttt-class-table后 继续处理后续的 com.ttt-class-table
	 * com.ttt-field-test
	 */
	NORMAL,
	/**
	 * 不再处理该类 的 同名 处理完com.ttt-class-table后 不再执行 后续的com.ttt-class-table
	 */
	STOP_NAME,
	/**
	 * 不再处理该类 处理完com.ttt-class-table后 不再执行 com.ttt-field 但是会执行后续的com.ttt-class-table 且可被后续返回值覆盖
	 */
	STOP_CLASS,
	/**
	 * 不再处理 处理完com.ttt-class-table 后该类处理完毕 不再处理
	 */
	STOP,
}

/**
 * 绑定结构 注解和处理器 传递!
 */
class Tracker {
	Class<? extends Annotation> cls; // 不泛型 ? extends Annotation 上转差别?
	OnAnnotation fun; // 回调处理
	ElementType type;	//Field Method Class
	public Tracker(Class<? extends Annotation> cls, ElementType type, OnAnnotation fun) {
		this.cls = cls;
		this.type = type;
		this.fun = fun;
	}
}

interface OnAnnotation{
	Status make(Annotation annotation, ElementType type, Object object, Class<?> cls);
}

///**
// * 绑定结构 class 注解 执行情况 是否停止 
// */
//class Classer {
//	public Class<?> cls;
//	public Status status;
//
//	public Classer(Class<?> cls, Status status) {
//		this.cls = cls;
//		this.status = status;
//	}
//}
