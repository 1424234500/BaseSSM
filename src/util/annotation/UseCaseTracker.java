package util.annotation;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.Tools;
import util.design.TestAnnotation;

/**
 * 注解案例
 * 目标注解捕获 处理器
 * 
 * 
 * 
 * 
 * @author walker
 *
 */

public class UseCaseTracker{
	public static void track(Class<?> cl){
		for(Method m : cl.getDeclaredMethods()){
			UseCase uc = m.getAnnotation(UseCase.class);
			if(uc != null){
				Tools.out("Use Case:", uc.id(), uc.description());
			}
		}
		
		
		
		
		
	}
	public static void main(String[] args){
		track( TestAnnotation.class);
	}
	
	
	
	
}