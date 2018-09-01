package util.annotation;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

import util.Tools;

/**
 * 注解案例
 * 目标注解捕获 处理器
 * 
 * 
 * @author walker
 *
 */

public class UseCaseTracker implements OnAnnotation{
	
	@Override
	public Status make(Annotation annotation, ElementType type, Object object, Class<?> cls) {
		Tools.out(this, annotation, type, object);

		UseCase instance = (UseCase)annotation;
//		Tools.out("", instance.id(), instance.description());
		
		return Status.NORMAL;
	}
	
 
	
	
	
}