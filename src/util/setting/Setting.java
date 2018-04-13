package util.setting;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import util.FileUtil;
import util.Tools;

/*
 * 配置文件 读写工具
 */
public class Setting {
	 public static Properties openProperties(String filename){
		 Properties proper = new Properties();     
	        try{
	            //读取属性文件a.properties
	        	FileUtil.mkfile(filename);
	            InputStream in = new BufferedInputStream (new FileInputStream(settingFileName));
	            proper.load(in);     ///加载属性列表 
	            in.close(); 
	        }
	        catch(Exception e){
	            e.printStackTrace();
	        }
		 return proper; 
	 }
	
	
	
	
	public static String settingFileName = "make.properties";
	private static Properties proper ;
	static  {
		proper = new Properties();     
        try{
            //读取属性文件a.properties
        	FileUtil.mkfile(settingFileName);
            InputStream in = new BufferedInputStream (new FileInputStream(settingFileName));
            proper.load(in);     ///加载属性列表 
            in.close(); 

        }
        catch(Exception e){
            e.printStackTrace();
        }
	}
	public static String getProperty(String key, String defaultValue ){
		if(defaultValue == null) defaultValue = "";
		return proper.getProperty(key, defaultValue);
	}
	
	/**
	 * 设置并写入文件 若为空”“则不处理
	 * @param msgType
	 * @param value
	 */
	public static void saveManyProperty(String...keyValues){
		if(keyValues == null || keyValues.length <= 1 || keyValues.length % 2 != 0){
			return;
		}
		for(int i = 0; i < keyValues.length; i+=2){
//			if(keyValues[i+1] == null || keyValues[i+1].equals("")){
//				continue;
//			}
			proper.setProperty(keyValues[i], keyValues[i+1]);
		} 
		///保存属性到b.properties文件
		try{
	        FileOutputStream oFile = new FileOutputStream(settingFileName, false);//true表示追加打开
	        proper.store(oFile, "Change at " + Tools.getNowTimeL());
	        oFile.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 设置并写入文件 若为空”“则不处理
	 * @param key
	 * @param value
	 */
	public static void saveProperty(String key, String value){
//		if(value == null || value.equals("")){
//			return;
//		}
		proper.setProperty(key, value);
        ///保存属性到b.properties文件
		try{
	        FileOutputStream oFile = new FileOutputStream(settingFileName, false);//true表示追加打开
	        proper.store(oFile, "Change at " + Tools.getNowTimeL());
	        oFile.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static String showString(){
		String res = "";
	    Iterator<String> it=proper.stringPropertyNames().iterator();
        while(it.hasNext()){
            String key=it.next();
            res += key + "=" + proper.getProperty(key);
        }
        return res;
	}
	
	
}
