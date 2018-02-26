package com.controller;

public class UtilTools {

	static public String  defaultFileUploadDir = "C:\\tomcat\\download";
	static public String  defaultFileDownloadDirs = "C:\\tomcat\\download,E:\\workspace_android,E:\\workspace_my\\cc\\ccandroid\\bin";

	static public String getUploadDir(){
		return util.setting.Setting.getProperty("fileUploadDir",defaultFileUploadDir);
	}
	static public String getScanDirs(){
		return util.setting.Setting.getProperty("fileScanDir",defaultFileDownloadDirs);
	}
	
	
	

	
	
}
