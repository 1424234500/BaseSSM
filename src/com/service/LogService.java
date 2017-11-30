package com.service;

import java.util.List;
import java.util.Map;

import com.mode.Page;


/**
 * 日志管理
 */
public interface LogService  {
	public void userMake(String userid, String url, String ip, String host, int port, String params);
	
}