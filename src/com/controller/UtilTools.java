package com.controller;

public class UtilTools {

	final static public String  defaultFileUploadDir = "C:\\tomcat\\download";
	final static public String  defaultFileDownloadDirs = "C:\\tomcat,E:\\workspace_android\\cc\\app\\build\\outputs\\apk\\debug";

	static public String getUploadDir(){
		return util.setting.Setting.getProperty("fileUploadDir",defaultFileUploadDir);
	}
	static public String getScanDirs(){
		return util.setting.Setting.getProperty("fileScanDir",defaultFileDownloadDirs);
	}
	
	
	

	
	
}
