package com.controller;

public class UtilTools {

	static public String  defaultFileUploadDir = "C:\\tomcat\\download";
	static public String  defaultFileDownloadDirs = "C:\\tomcat,E:\\workspace_android\\cc\\app\\build\\outputs\\apk\\debug";

	static public String getUploadDir(){
		return util.setting.Setting.getProperty("fileUploadDir",defaultFileUploadDir);
	}
	static public String getScanDirs(){
		return util.setting.Setting.getProperty("fileScanDir",defaultFileDownloadDirs);
	}
	
	
	

	
	
}
