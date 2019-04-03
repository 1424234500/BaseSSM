package com.walker.core.scheduler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.walker.common.util.ClassUtil;

/**
 * 用于任务调度的任务
 * 
 * 定时执行某个函数
 *
 */
public class Task {
	/**
	 * id 任务id标识
	 */
	String id;
	
	/**
	 * com.task.TaskTest
	 */
	String className; 
	
	/**
	 * 这是个测试任务
	 */
	String about;
	
	/**
	 * 任务添加 使用job triiger
	 */
	JobDetail jobDetail;
	Trigger trigger;
	
	/**
	 * 多触发器 
	 * CRON表达式    含义 
	"0/10 * * * * ?"     
	"0 0 12 * * ?"    每天中午十二点触发 
	"0 15 10 ? * *"    每天早上10：15触发 
	"0 15 10 * * ?"    每天早上10：15触发 
	"0 15 10 * * ? *"    每天早上10：15触发 
	"0 15 10 * * ? 2005"    2005年的每天早上10：15触发 
	"0 *  14 * * ?"    每天从下午2点开始到2点59分每分钟一次触发 
	"0 0/5 14 * * ?"    每天从下午2点开始到2：55分结束每5分钟一次触发 
	"0 0/5 14,18 * * ?"    每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发 
	"0 0-5 14 * * ?"    每天14:00至14:05每分钟一次触发 
	"0 10,44 14 ? 3 WED"    三月的每周三的14：10和14：44触发 
	"0 15 10 ? * MON-FRI"    每个周一、周二、周三、周四、周五的10：15触发
	 */
	Set<String> pattern;

	
	public Task(){
		pattern = new HashSet<>();
	}
	/**
	 * 构造一个任务
	 * @param className
	 * @param methodName
	 * @param args
	 */
	public Task(String className, String about, String...crons){
		this();
		this.className = className;
		this.about = about;
		pattern.addAll(Arrays.asList(crons));
	}
	/**
	 * 以反射 类名 函数名 和参数名作为统一 id
	 */
	@Override
	public String toString() {
		return "Task:" + className + " [" + about + "]";
	}
	public void addCron(String str) {
		this.pattern.add(str);
	}
	public Boolean removeCron(String str) {
		return this.pattern.remove(str);
	}
	
	
	

	JobDetail getJobDetail(){
		String name = this.toString();
		Class clz = ClassUtil.loadClass(className);
		this.jobDetail = JobBuilder
			.newJob (clz)
			.withIdentity(name)
			.withDescription(this.about)
			.build();
			
		return this.jobDetail;
	}
	
	Trigger getTrigger(){
		TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();

		Set<String> trr = this.pattern;
		for(String cron : trr){
			triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
		}
		this.trigger = triggerBuilder.build();
		return this.trigger;
	}
	
	
}
	
