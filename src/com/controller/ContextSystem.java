package com.controller;

import util.cache.CacheMgr;

/**
 * 系统共用环境 结合缓存 统一接口 避免多处直接查询缓存
 * 缓存设置值来源于 系统初始化 时加载的 配置文件 或者 数据库
 *
 */
public class ContextSystem {

	/**
	 * 默认数据库操作一次大小
	 */
	public static int getDbOnce(){
		return CacheMgr.getInstance().get("DB_ONCE", 500);
	}
	
	
	
	
	
	
}
