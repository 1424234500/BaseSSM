package com.service;

import java.util.List;
import java.util.Map;

import com.controller.Page;


/**
 * 日志管理
 */
public interface LogService  {
	//登陆用户操作 记录
	public void userMake(String userid, String url, String ip, String host, int port, String params);
	//所有请求时间花费 统计 redis
	public void exeStatis(String url, String params, long costtime);
	//把统计在redis中的数据导出到oracle
	public void saveStatis();
		
}