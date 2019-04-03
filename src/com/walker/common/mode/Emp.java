package com.walker.common.mode;


/**
 * 基本对象 员工 并测试继承多态static执行顺序
 * @author Walker
 * 2017年10月30日09:50:08
 */
public class Emp {
	
	public String id = "test";
	String name;
	String dept;
	static{
		System.out.println("emp static{}");
	}
	public Emp(){
		System.out.println("emp init");

		
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	
	public void fun(){
		System.out.println("emp fun this.id:" + this.id  );
	}
	
}
