package com.walker.service;

import org.apache.log4j.Logger;

/**
 * 日志管理
 */
public interface LogService  {
	Logger logger = Logger.getLogger(LogService.class); 
	String CACHE_KEY = "cache-url-request";
	
	
	//登陆用户操作 记录
	public void userMake(String userid, String url, String ip, String host, int port, String params);
	//所有请求时间花费 统计 redis
	public void exeStatis(String url, String params, long costtime);
	//把统计在redis中的数据导出到oracle
	public void saveStatis();
		
}