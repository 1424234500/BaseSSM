package com.walker.common.study;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设计模式学习
 * 
1、开闭原则（Open Close Principle）

开闭原则就是说对扩展开放，对修改关闭。在程序需要进行拓展的时候，不能去修改原有的代码，实现一个热插拔的效果。所以一句话概括就是：为了使程序的扩展性好，易于维护和升级。想要达到这样的效果，我们需要使用接口和抽象类，后面的具体设计中我们会提到这点。

2、里氏代换原则（Liskov Substitution Principle）

里氏代换原则(Liskov Substitution Principle LSP)面向对象设计的基本原则之一。 里氏代换原则中说，任何基类可以出现的地方，子类一定可以出现。 LSP是继承复用的基石，只有当衍生类可以替换掉基类，软件单位的功能不受到影响时，基类才能真正被复用，而衍生类也能够在基类的基础上增加新的行为。里氏代换原则是对“开-闭”原则的补充。实现“开-闭”原则的关键步骤就是抽象化。而基类与子类的继承关系就是抽象化的具体实现，所以里氏代换原则是对实现抽象化的具体步骤的规范。—— From Baidu 百科

3、依赖倒转原则（Dependence Inversion Principle）

这个是开闭原则的基础，具体内容：真对接口编程，依赖于抽象而不依赖于具体。

4、接口隔离原则（Interface Segregation Principle）

这个原则的意思是：使用多个隔离的接口，比使用单个接口要好。还是一个降低类之间的耦合度的意思，从这儿我们看出，其实设计模式就是一个软件的设计思想，从大型软件架构出发，为了升级和维护方便。所以上文中多次出现：降低依赖，降低耦合。

5、迪米特法则（最少知道原则）（Demeter Principle）

为什么叫最少知道原则，就是说：一个实体应当尽量少的与其他实体之间发生相互作用，使得系统功能模块相对独立。

6、合成复用原则（Composite Reuse Principle）

原则是尽量使用合成/聚合的方式，而不是使用继承。 *
 */
public class DesignMode{
	
	
	
	
}

interface User{
	void show();
}
class Student implements User{public void show() {System.out.println("show student");}}
class Teacher implements User{public void show() {System.out.println("show teacher");}}
enum Type{
	STUDENT, TEACHER
}
/**
 * 工厂模式 上转接口 抽象 构造器
 * FactoryMethod.getUser(Type.STUDENT)
 */
class FactoryMethod{
	public static User getStudent() {return new Student();}
	/**
	 * 修改参数化
	 * @param type
	 * @return User
	 */
	public static User getUser(Type type) { 
		switch (type) {
		case STUDENT:
			return new Student();
		}
		return null;
	}
}

/**
 * 单例
 * Singleton.getInstance()
 */
class Singleton {
	
    static Singleton instance;
    
    /**
     * 锁静态模式
     */
    public static Singleton getInstanceSy() {  
        if (instance == null) {  
            synchronized (instance) {  
                if (instance == null) {  
                    instance = new Singleton();  //隐患 在Java指令中创建对象和赋值操作是分开进行的，也就是说instance = new Singleton();语句是分两步执行的。但是JVM并不保证这两个操作的先后顺序，也就是说有可能JVM会为新的Singleton实例分配空间，然后直接赋值给instance成员
                }  
            }  
        }  
        return instance;  
    }  
    
    
    /**
	 * 私有构造器
	 */
    private Singleton(){}
    /**
     * 私有静态内部类
     */
    private static class SingletonFactory{           
        private static Singleton instance;
        static {
        	System.out.println("静态内部类初始化" + SingletonFactory.class);
        	instance = new Singleton();  
        }
    }
    /**
     * 内部类模式 可靠
     */
    public static Singleton getInstance(){           
        return SingletonFactory.instance;           
    }  
    
}
/**
 * Builder 函数返回self 
 * new Builder().setName("name").setAge("19");
 */
class Builder{
	Builder setName(String name) {return this;};
	Builder setAge(String age) {return this;};
	
}
/**
 * 原型模式 浅拷贝，关于深浅拷贝 实现深复制，需要采用流的形式读入当前对象的二进制输入，再写出二进制数据对应的对象
 * new Prototype().clone();
 */
class Prototype implements Cloneable{
    @Override
    protected Object clone()   {
        try {
            return super.clone(); //super.clone()调用的是Object的clone()方法，而在Object类中，clone()是native的
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }finally {
        	
        }
        return null;
    }
}
/**
 * 装饰模式 实现相同接口 传递父类 环绕执行
 * new  Decorator ( new Student()).show();
 * new  Decorator ( new Teacher()).show();
 */
class  Decorator implements User{
	User user;
	 Decorator(User user){
		this.user = user;
	}
	public void show() {
		System.out.println("before show");
		this.user.show();
		System.out.println("after show");
	}
}
/**
 * 享元模式 元素 初始化管理 动态管理 连接池
 * pool = new Flyweight()
 * pool.get("student");
 * 结合 enumMap
 */
class Flyweight{
	private Map<String, Object> map;
	public Object get(String key) {
		if(map.containsKey(key))return map.get(key);
		else {
			Object obj = new Student();
			map.put(key, obj);
			return obj;
		}
	}
}

/**
 * 外观模式 统一组合
 */
class School{
	Student student = new Student();
	Teacher teacher = new Teacher();
	public void show() {
		student.show();
		teacher.show();
	}
}

/**
 * 观察者模式
 */
class Observer{
	List<User> users = new ArrayList<>();
	void start() {
		users.add(new Student()); //订阅
		users.add(new Teacher()); //
		notifyAll();	//发布唤醒
	}
	public void notifyUsers(){
		for(User user : users) {
			user.show();
		}
	}
}
/**
 * 处理链模式 下一步交由下一个人 链表linkedList
 */ 
class Handler{
	Handler nextHandler;
	User user;
	
	public void show() {
		user.show();
		if(1 < 2 && nextHandler != null) { //是否执行下一个步骤 中断
			nextHandler.show();
		}
	}
}


/**
 * 状态模式 根据不同属性执行不同方法 分支 if else ?
 */








