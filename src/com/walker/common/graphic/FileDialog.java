package com.walker.common.graphic;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;

import com.walker.common.util.Tools;
 
/**
 * This class demonstrates the DirectoryDialog class
 */
public class FileDialog {
    
	public static String getDirPath(String nowdir){
		try{
			  JFileChooser jfc=new JFileChooser();  
		      jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );  
		      if(Tools.notNull(nowdir))
		    	  jfc.setCurrentDirectory(new File(nowdir));
		      jfc.showDialog(new JLabel(), "选择文件夹");  
		      
		      File file=jfc.getSelectedFile();  
		      if(file.isDirectory()){  
		          Tools.out("文件夹:"+file.getAbsolutePath());  
	          }else if(file.isFile()){  
	              Tools.out("文件:"+file.getAbsolutePath());  
	          }  
		      return file.getAbsolutePath();
		}catch(Exception e){
	    	  return nowdir;
	    }
	}
	public static String getDirOrFilePath(String nowdir){
		try{
		  JFileChooser jfc=new JFileChooser();  
	      jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
	      if(Tools.notNull(nowdir))
	    	  jfc.setCurrentDirectory(new File(nowdir));
	      jfc.showDialog(new JLabel(), "选择文件夹或者文件");  
	      File file=jfc.getSelectedFile();  
	      if(file.isDirectory()){  
	          Tools.out("文件夹:"+file.getAbsolutePath());  
        }else if(file.isFile()){  
            Tools.out("文件:"+file.getAbsolutePath());  
        }  
	      return file.getAbsolutePath();
	}catch(Exception e){
  	  return nowdir;
  }
	}
	public static String getFilePath(String nowdir){
		try{
		    JFileChooser jfc=new JFileChooser();  
	        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );  
	        if(Tools.notNull(nowdir))
		    	  jfc.setCurrentDirectory(new File(nowdir));
	        jfc.showDialog(new JLabel(), "选择文件");  
	        File file=jfc.getSelectedFile();  
	        if(file.isDirectory()){  
	            Tools.out("文件夹:"+file.getAbsolutePath());  
	        }else if(file.isFile()){  
	            Tools.out("文件:"+file.getAbsolutePath());  
	        }  
			return file.getAbsolutePath();  
		}catch(Exception e){
	  	  return nowdir;
	  }
	}
	
	 
  
}
