package com.walker.core.annotation;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.walker.common.util.ClassUtil;
import com.walker.common.util.Tools;

/**
 *	Test注解处理
 *	
 *	反射处理项目所有文件 
 *
 */

public class TestTracker implements OnAnnotation{
	
	@Override
	public Status make(Annotation annotation, ElementType type, Object object, Class<?> cls) {
//		Tools.out(this, annotation, type, object);
//		Test instance = (Test)annotation;
		
		if(type.equals(ElementType.METHOD)){
			Method method = (Method)object;
			
			//空返回 或者 bool 则执行 并判定测试结果
			if(method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class) || method.getReturnType().equals(void.class)){
				boolean res = false;
				//开启断言
				ClassLoader.getSystemClassLoader().setClassAssertionStatus(method.getDeclaringClass().getName(), true);
				//开启访问权限private
				method.setAccessible(true);
				try {
					if(! method.getReturnType().equals(void.class) )
						res = (Boolean) ClassUtil.doClassMethod(cls, method);
					else{
						ClassUtil.doClassMethod(cls, method);
						res = true;
					}
				} catch (Exception e) {
//					e.printStackTrace();
					res = false;
				}
				if(res){
					Tools.out("TestTracker", cls.getName(), method.getName(), "true---------");
				}else{
					Tools.out("TestTracker", cls.getName(), method.getName(), "false--xxxxxxxxxxx");
				}

			}else{
				Tools.out(cls.getName() + " " + method.getName() + " test测试方法 必须boolean 或者 无返回值 且无参数");
			}
			
			
		}
		
		
		return Status.NORMAL;
	} 
	
	
	
}