package com.walker.core.annotation;
import java.lang.annotation.*;

/**
 * 注解
 * 看起来就像接口的方法, 唯一的区别是你可以为其指定默认值
 * 
 * @Target 表示注解可用于什么地方 
 *  CONSTRUCTOR构造器 
 *  FIELD域声明包括enum实例
 *  METHOD 函数
 *  LOCAL_VARIABLE局部变量声明 
 *  PACKAGE包声明 
 *  PARAMETER参数声明 
 *  TYPE类 接口 enum声明 
 * 不写可以用于所有ElementType?
 * @Retention 表示需要什么级别保存该注解信息
 *  SOURCE注解被编译器丢弃 
 *  CLASS注解class文件可用 被VM丢弃 
 *  RUNTIME vm在运行期也保留 可以通过反射获取信息
 * @Documented 将此注解包含在Javadoc中
 * @Inherited 允许子类继承父类中的注解
 * 
 * 数据库bean映射
 * 
 * 域注解 运行时
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBSQLString {
	
	/**
	 * 如果命名 为value 仅此 并且是没有其他需要赋值的 则可以 使用注解 时省略键名 直接写值 
	 */
	int value() default 0; 
	String name() default "";
	DBConstraints DBConstraints() default @DBConstraints;
	
	
}
