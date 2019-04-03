package com.walker.web.controller;

import com.walker.core.cache.CacheMgr;

/**
 * 系统共用环境 结合缓存 统一接口 避免多处直接查询缓存 缓存设置值来源于 系统初始化 时加载的 配置文件 或者 数据库
 *
 */
public class ContextSystem {

	/**
	 * 默认数据库操作一次大小
	 */
	public static int getDbOnce() {
		return CacheMgr.getInstance().get("DB_ONCE", 500);
	}
	/**
	 * 默认分页每页数量
	 */
	public static int getShowNum() {
		return CacheMgr.getInstance().get("SHOW_NUM", 5);
	}

	final static public String defaultFileUploadDir = "C:\\tomcat\\download";
	final static public String defaultFileDownloadDirs = "C:\\tomcat,E:\\workspace_android\\cc\\app\\build\\outputs\\apk\\debug";

	static public String getUploadDir() {
		return CacheMgr.getInstance().get("FILE_UPLOAD_DIR", defaultFileUploadDir);
	}

	static public String getScanDirs() {
		return CacheMgr.getInstance().get("FILE_SCAN_DIR", defaultFileDownloadDirs);
	}

}
