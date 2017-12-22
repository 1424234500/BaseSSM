package com.controller;

public class UtilTools {

	static public String  defaultFileUploadDir = "C:\\tomcat\\download\\";

	static public String getDir(){
		return util.setting.Setting.getProperty("fileUploadDir",defaultFileUploadDir);
	}
	
	
	

	
	
}
