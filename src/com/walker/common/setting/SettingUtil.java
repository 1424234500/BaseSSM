package com.walker.common.setting;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import com.walker.common.util.Bean;
import com.walker.common.util.FileUtil;
import com.walker.common.util.MapListUtil;
import com.walker.common.util.Tools;

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
        Bean res = new Bean();
        
        for(Object item : bean.keySet()){
        	MapListUtil.putMapUrl(res, item.toString(), bean.get(item));
        }
        
        return res;
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
