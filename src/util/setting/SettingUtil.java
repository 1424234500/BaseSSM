package util.setting;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import util.Bean;
import util.FileUtil;
import util.MapListUtil;
import util.Tools;

/**
 * 配置文件properties读写工具
 * xxx.properties <-> Bean
 * xxx.properties <-> Key-Value
 * 
 */
public class SettingUtil {
	
	public static Bean getSetting(String filename){
		Properties proper = new Properties();     
        try{
            InputStream in = new BufferedInputStream (new FileInputStream(filename));
            proper.load(in);
            in.close(); 
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Bean bean = new Bean(proper); 
        return bean;
	}
	public static void saveSetting(String filename, Bean bean){
		Properties proper = new Properties();     
		proper.putAll(bean);
		try{
	        FileOutputStream oFile = new FileOutputStream(filename, false);//true表示追加打开
	        proper.store(oFile, "Change at " + Tools.getNowTimeL());
	        oFile.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	} 
	
}
