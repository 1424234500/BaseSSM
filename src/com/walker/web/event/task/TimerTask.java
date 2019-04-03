package com.walker.web.event.task;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.walker.service.FileService;
import com.walker.service.LogService;

/**
 * 定时器任务
 */
@Component
public class TimerTask {
	static private Logger log = Logger.getLogger("spring.TimerTask"); 
	static private long count = 0;
	
	@Scheduled(cron = "0 59 23 ? * *") //每天
	public void EveryDay() {
	    log.info("[eachDay 23:59][每天任务]");
	}

	
	
	@Autowired
	@Qualifier("fileService") 
	FileService fileService;
	
	@Autowired
	@Qualifier("logService") 
	LogService logService;
	
	@Scheduled(cron = "0 0/60 * * * ?") //每小时
	public void EveryHour() {
	    log.info("[eachHour 59m][每小时任务]");
	    
	    log.info("扫描同步上传文件"); 
	    //刷新上传文件集合的 文件数据到 内存数据库？ 文件管理系统 展示文件 介绍（图片），
	    fileService.scan();
	}

	
	@Scheduled(cron = "0/60 * * * * ?") //每分钟
	public void eachMinute() {
//	    log.info("[eachMinute 0/60 * * * * ?][每分钟任务]");
//		log.info("Redis操作记录持久化"); 
	    //刷新redis到oracle
	    logService.saveStatis();
	    fileService.scan();

	}

	@Scheduled(cron = "0/10 * * * * ?") //每10s
	public void eachMake() {
//	    log.info(count++);
	}
	
	/*
	cronExpression的配置说明，具体使用以及参数请百度google
	字段   允许值   允许的特殊字符
	秒    0-59    , - * /
	分    0-59    , - * /
	小时    0-23    , - * /
	日期    1-31    , - * ? / L<last> W C
	月份    1-12 或者 JAN-DEC    , - * /
	星期    1-7 或者 SUN-SAT    , - * ? / L C #
	年（可选）    留空, 1970-2099    , - * / 
	- 区间  
	* 通配符  
	? 你不想设置那个字段
	 
	CRON表达式    含义 
	"0 0 12 * * ?"    每天中午十二点触发 
	"0 15 10 ? * *"    每天早上10：15触发 
	"0 15 10 * * ?"    每天早上10：15触发 
	"0 15 10 * * ? <*>"    每天早上10：15触发 
	"0 15 10 * * ? 2005"    2005年的每天早上10：15触发 
	"0 *  14 * * ?"    每天从下午2点开始到2点59分每分钟一次触发 
	"0 0/5 14 * * ?"    每天从下午2点开始到2：55分结束每5分钟一次触发 
	"0 0/5 14,18 * * ?"    每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发 
	"0 0-5 14 * * ?"    每天14:00至14:05每分钟一次触发 
	"0 10,44 14 ? 3 WED"    三月的每周三的14：10和14：44触发 
	"0 15 10 ? * MON-FRI"    每个周一、周二、周三、周四、周五的10：15触发 
	*
	*
	*/
	
	
}
