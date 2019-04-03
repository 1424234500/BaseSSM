package com.walker.service;

/**
 * 日志管理
 */
public interface FileService  {
	/**
	 * 扫描文件夹下文件信息 并存入数据库记录
	 */
	public void scan();
	/**
	 * 上传文件
	 */ 
	public String upload(String id, String name, String path, String about);
	
	/**
	 * 文件上传或者下载记录 fileId up/down 耗时detaTime
	 */
	public int fileUpDown(String fileId, String type, String detaTime);
	/**
	 * 初始化文件目录
	 */
	public void initDirs();
	
	
	
}