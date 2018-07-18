package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {

	/**
	 * 文件集合排序
	 * @param tempList 源files
	 * @param order 排序参数time name
	 * @param orderRe 是否倒序
	 * @return
	 */
	public File[] orderBy(File[] tempList,   String order,   boolean orderRe) {
		if(order == null)order = "name";
		if(order.equals("no"))return tempList;
		if(order.equals("null"))return tempList;
		
		final String or = order;
		final boolean orRe = orderRe;
		
		List<File> sortfiles = Arrays.asList(tempList);  
		Collections.sort(sortfiles, new Comparator<File>(){  
		    public int compare(File o1, File o2) {  
		    	int res = 0;
		    	if(or.equals("time")){
	    		   String str1 = String.valueOf(o1.lastModified());  
		           String str2 = String.valueOf(o2.lastModified());  
		           res = str1.compareTo(str2);
		    	}else 	if(or.equals("name")){
		    		res = o1.getName().compareTo(o2.getName());  
		    	} 
		    	
		    	if(orRe){
		    		res = 0-res;
		    	}
		           
	           return res;   
		    }  
		});  
		for(int i = 0; i < sortfiles.size(); i++){
			tempList[i] = sortfiles.get(i);
		}
		
		return tempList;
	}

	public static boolean mkfile(String path){
		if(path == null)return false;
		File file = new File(path);
		if(file.exists() || file.isDirectory()){
			return  true;
		}else{ 
			try {
				return file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} 
	}
	public static boolean mkdir(String dir){
		if(dir == null)return false;
		File file = new File(dir);
		if(file.exists()){
			return  true;
		}
		return file.mkdirs();
	}
	/**
	 * 遍历处理目录下的文件
	 * @param dir
	 * @param cfun
	 * @return 文件的总数
	 */
	public static int controlDirs(String dir, Fun<File> cfun){
		List< File > files = getAllFiles(dir);
		for(File f : files){
			cfun.make(f);
		}
		return files.size();
	}
	
	/**
	 * 同步
	 * 树形展示目录及其文件   获取文件夹下所有的文件
	 * @param dir
	 * @param funFileOrDir 遍历时处理回调可为空
	 * @return 文件list 及目录都有
	 */
	public static List<File> getAllFilesAsync(String dir){
		return showDirAsync(dir, null);
	}
	/**
	 * 树形展示目录及其文件   获取文件夹下所有的文件
	 * @param dir
	 * @param funFileOrDir 遍历时处理回调可为空
	 * @return 文件list 及目录都有
	 */
	public static List<File> getAllFiles(String dir){
		return showDir(dir, null);
	}
	/**
	 * 获取或操作 所有文件夹和文件 同步  dir可 ,分割多个dir
	 * @param dir
	 * @param funFileOrDir
	 */
	public static List<File> showDirAsync(String dire, final Fun<File> funFileOrDir){ 
		final List< File >files=new ArrayList<File>();
		String[] dirs = dire.split(",");
		for(final String dir : dirs){
			longErgodic(new File(dir), files, funFileOrDir);//把遍历得到的东西存放在files里面
		}
		return files;
	}
	/**
	 * 获取或操作 所有文件夹和文件 线程池异步 dir可 ,分割多个dir
	 * @param dir
	 * @param funFileOrDir
	 */
	public static List<File> showDir(String dire, final Fun<File> funFileOrDir){ 
		final List< File >files=new ArrayList<File>();
		String[] dirs = dire.split(",");
		for(final String dir : dirs){
			ThreadUtil.execute(new Runnable(){
				public void run(){
					longErgodic(new File(dir), files, funFileOrDir);//把遍历得到的东西存放在files里面
				}
			});		
		}
		return files;
	}
	/**
	 * 递归遍历目录
	 * @param file 当前处理文件
	 * @param files 结果集
	 * @param funFileOrDir 回调函数处理
	 */
	private static void longErgodic(File file, final List<File> files, final Fun<File> funFileOrDir) { 
		if (file == null || ! file.exists())
			return;
		files.add(file);	//添加当前 节点
		if(file.isDirectory()){ //若是文件夹 则递归子节点 深度优先
			File[] fillArr = file.listFiles();
			if (fillArr == null)
				return;
			for (File file2 : fillArr) {
				longErgodic(file2, files, funFileOrDir);//把遍历得到的东西存放在files里面
			}
		}
		if (funFileOrDir != null) { //处理当前节点
			funFileOrDir.make(file);
		}
	}
	 
    /**
     *  以字节为单位读取文件，通常用于读取二进制文件，如图片
     * @param path
     * @return
     */
    public static String readByBytes(String path, Fun<String> fun) {    
        String content = null;
        
        try {
            InputStream inputStream = new FileInputStream(path);
            StringBuffer sb = new StringBuffer();
            int c = 0;
            byte[] bytes = new byte[1024];
            /*
             * InputStream.read(byte[] b)
             * 
             * Reads some number of bytes from the input stream and stores them into the buffer array b. 从输入流中读取一些字节存入缓冲数组b中
             * The number of bytes actually read is returned as an integer.  返回实际读到的字节数
             * This method blocks until input data is available, end of file is detected, or an exception is thrown. 
             * 该方法会一直阻塞，直到输入数据可以得到、或检测到文件结束、或抛出异常  -- 意思是得到数据就返回
             */
            String temp = "";
            		
            if(fun != null)
	            while ((c = inputStream.read(bytes)) != -1) {
	            	temp = new String(bytes, 0, c, "utf-8");
	                sb.append(temp);
	                fun.make(temp);
	            }
            else
            	 while ((c = inputStream.read(bytes)) != -1) {
                     sb.append(new String(bytes, 0, c, "utf-8"));
                 }
            
            content = sb.toString();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return content;
    } 
   
    /**
     *  以行为单位读取文件，常用于读取面向行的格式化文件
     * @param path
     * @return
     */
    public static String readByLines(String path, Fun<String> fun) {
        String content = null;
        
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
            
            StringBuffer sb = new StringBuffer();
            String temp = null;
            if(fun != null)
	            while ((temp = bufferedReader.readLine()) != null) {
	                sb.append(temp); 
	                fun.make(temp);
	            }
            else
            	while ((temp = bufferedReader.readLine()) != null) {
  	                sb.append(temp); 
  	            }
            content = sb.toString();
            bufferedReader.close();
        } catch (UnsupportedEncodingException  e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return content;
    }
    /**
     *  以字符为单位读取文件，常用于读取文本文件
     * @param path
     * @return
     */
    public static String readByChars(String path, Fun<char[]> fun) {
        String content = null;
        
        try {
            
            Reader reader = new InputStreamReader(new FileInputStream(path), "utf-8");
            StringBuffer sb = new StringBuffer();
            

            char[] tempchars = new char[1024];
            if(fun == null)
	            while (reader.read(tempchars) != -1) {
	                sb.append(tempchars);
	            }
            else
            	 while (reader.read(tempchars) != -1) {
 	                sb.append(tempchars);
 	                fun.make(tempchars);
 	            }
            
            content = sb.toString();
            reader.close();    
        } catch (Exception e) {
            e.printStackTrace();
        }    
        return content;
    }
    
    /**
     *  把内容content写的path文件中
     * @param content
     * @param path
     * @return
     */
    public static boolean saveAs(String content, String path) {
        
        FileWriter fw = null;
        
        //out("把内容：" + content + "， 写入文件："  + path);
        
        try {
            /**
             * Constructs a FileWriter object given a File object. 
             * If the second argument is true, then bytes will be written to the end of the file rather than the beginning.
             * 根据给定的File对象构造一个FileWriter对象。 如果append参数为true, 则字节将被写入到文件的末尾（向文件中追加内容）
             *
             *    Parameters:
             *        file,  a File object to write to 带写入的文件对象
             *        append,  if true, then bytes will be written to the end of the file rather than the beginning
             *    Throws:
             *        IOException - 
             *        if the file exists but is a directory rather than a regular file, 
             *            does not exist but cannot be created, 
             *            or cannot be opened for any other reason
             *      报异常的3种情况：
             *          file对象是一个存在的目录（不是一个常规文件）
             *          file对象是一个不存在的常规文件，但不能被创建
             *          file对象是一个存在的常规文件，但不能被打开
             *
             */
            fw = new FileWriter(new File(path), false);
            if (content != null) {
                fw.write(content);
            }    
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    
    /**
     * 按照文件类型读取
     * path
     * .xls .xlsx 表格
     * .c .txt .python 文本类型
     */
    public static Object readByType(String path, Fun<String> fun, Fun<ArrayList<ArrayList<Object>>> excel){
    	if(check(path) != 0) {
    		return "false";
    	}
    	String ext = getFileType(path);
    	if(ext.equals("txt") || 
    			ext.equals("c") || 
    			ext.equals("cpp") || 
    			ext.equals("html") || 
    			ext.equals("jsp") || 
    			ext.equals("java") || 
    			ext.equals("class")  ){
			
			 return  readByLines(path, fun);
    	}else if(ext.equals("xls") || ext.equals("xlsx")){
    		ArrayList<ArrayList<Object>> res = ExcelUtil.readExcel(new File(path));
    		if(excel != null)
    			excel.make(res);
    		return res;
    	}else{
    		return "File:" + path + " 不能识别";
    	}
    }
    
    
    
    /** 
	* 复制单个文件 
	* @param oldPath String 原文件路径 如：c:/fqf.txt 
	* @param newPath String 复制后路径 如：f:/fqf.txt 
	* @return boolean 
	*/ 
	public static void copyFile(String oldPath, String newPath) { 
	try { 
		mkdir(getFilePath(newPath));
		
		int bytesum = 0; 
		int byteread = 0; 
		File oldfile = new File(oldPath); 
		if (oldfile.exists()) { //文件存在时 
			InputStream inStream = new FileInputStream(oldPath); //读入原文件 
			FileOutputStream fs = new FileOutputStream(newPath); 
			byte[] buffer = new byte[1444]; 
			int length; 
			while ( (byteread = inStream.read(buffer)) != -1) { 
				bytesum += byteread; //字节数 文件大小 
				//out(bytesum); 
				fs.write(buffer, 0, byteread); 
			} 
			inStream.close(); 
		} 
	} 
	catch (Exception e) { 
		out("复制单个文件操作出错"); 
		e.printStackTrace(); 
	} 

} 

	/** 
	* 复制整个文件夹内容 
	* @param oldPath String 原文件路径 如：c:/fqf 
	* @param newPathth String 复制后路径 如：f:/fqf/ff 
	* @return boolean 
	*/ 
	public static void copyFolder(String oldPath, String newPath) { 

		try { 
		(new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
		File a=new File(oldPath); 
		String[] file=a.list(); 
		File temp=null; 
		for (int i = 0; i < file.length; i++) { 
			if(oldPath.endsWith(File.separator)){ 
				temp=new File(oldPath+file[i]); 
			} 
			else{ 
				temp=new File(oldPath+File.separator+file[i]); 
			} 
		
			if(temp.isFile()){ 
				FileInputStream input = new FileInputStream(temp); 
				FileOutputStream output = new FileOutputStream(newPath + "/" + 
				(temp.getName()).toString()); 
				byte[] b = new byte[1024 * 5]; 
				int len; 
				while ( (len = input.read(b)) != -1) { 
					output.write(b, 0, len); 
				} 
				output.flush(); 
				output.close(); 
				input.close(); 
			} 
			if(temp.isDirectory()){//如果是子文件夹 
				copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]); 
			} 
		} 
		} 
		catch (Exception e) { 
		//out("复制整个文件夹内容操作出错"); 
		e.printStackTrace(); 
	
		} 

	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	/**
	 * 扫描文件夹下ext类型的文件信息，获取路径，名字，大小
	 * @param dir
	 * @param ext
	 * @return
	 */
	public static List<Map<String,Object>> getDirFiles( String dir, String ext){
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		
		// 扫描文件夹 读取数据
		out("扫描文件目录" + dir);
		File rootfile = null;
		try {
			rootfile = new File(dir);
		} catch (Exception e) {
			out("打开文件目录[" + dir + "] error ");
		}
		File coders[] = rootfile.listFiles(); // 用户各自文件夹名集合
		if (coders == null) {
			out("文件夹为null");
		}else{
			for (File coder : coders) {
				if (coder.isFile()) {
					// 后缀判定xxx.apk, about.txt, xxx.img
					String fileName = coder.getName();
					if(getFileType(fileName).equals(ext)){
						res.add(fileToMap(coder));
					}
				}
	
			}
		}
		
		return res;
	}
	
	/**
	 * 获取某路径下所有文件 文件夹 ll
	 */
	public static List<Map> ls(String dir){
		List<Map> res = new ArrayList<Map>();
		
		out("扫描文件目录:" + dir);
		File rootfile = new File(dir);
		File coders[] = rootfile.listFiles();  
		int countFile = 0;
		for (File coder : coders) {
			if (coder.isDirectory()) {
				res.add(fileToMap(coder));
			}
		}
		for (File coder : coders) {
			if (coder.isFile()) {
				countFile++;
				res.add(fileToMap(coder));
			}
		}
		out("文件夹数量:" + (coders.length - countFile) + " \t文件数量:" + countFile);
		return res;
	}
	/**
	 * 获取某文件详情
	 */
	public static Map getFileMap(String file){
		if(file != null && file.length() > 0){
			File rootfile = new File(file);
			if(rootfile.exists()){
				return fileToMap(rootfile);
			}
		}
		return new HashMap();
	}
	
	/**
	 * 获取文件 map样式键集合
	 */
	public static List getFileMap(){
		List<String> res = new ArrayList<String>();
		res.add("PATH");
		res.add("NAME");
		res.add("SIZE");
		res.add("LENGTH");
		res.add("TIME");
		res.add("TYPE");
		res.add("CHILDS");
		return res;
	}
	public static Map fileToMap(File coder){
		Map map = new LinkedHashMap<String, Object>();
		map.put("PATH", coder.getAbsolutePath());
		map.put("NAME", coder.getName());
		map.put("SIZE", calcSize(coder.length()));
		map.put("LENGTH",  coder.length());
		map.put("TIME", coder.lastModified());
		
		String type = "";
		int dirfiles = 0;
		if(coder.isFile()){
			type = getFileType(coder.getAbsolutePath());
		}else if(coder.isDirectory()){
			type = "dir";
			File[] f =  coder.listFiles();
			if(f != null)
				dirfiles =f.length;
		}
		map.put("TYPE", type);
		map.put("CHILDS", dirfiles);

		return map;
	}
	
	//通过字符串长度，计算大概的 流量大小 MB KB B char=B
	static String calcSize(long length) {
		long m = length/(1024*1024);
		long k = length%(1024*1024)/1024;
		long b = length%(1024*1024)%1024;
		return m>0?  m+"."+k/100+"MB" : k>0? k+"."+b/100+"KB" : b+"B";
	}
	static String calcSize(int length) {
			int m = length/(1024*1024);
			int k = length%(1024*1024)/1024;
			int b = length%(1024*1024)%1024;
			return m>0?  m+"."+k/100+"MB" : k>0? k+"."+b/100+"KB" : b+"B";
		}
	
	/**
	 * 检查文件类型 
	 * @param path
	 * @return 0 文件 1文件夹 -1异常
	 */
	public static int check(String path){
		int res = -1;
		File file = new File(path);
		if(file.exists()){
			if(file.isFile()){
				res = 0;
			}else if(file.isDirectory()){
				res = 1;
			}else{
				out("文件：" + path + "不存在或者不是文件");
			}
		}else{
			out("文件：" + path + "不存在或者不是文件");
		}
		return res;
	}
	//删除附件，Eg: Constant.fileupload目录,xxx-xxx.doc 则删除该目录下所有xxx-xxx.doc/exe/dll
  	public static boolean delete(String dir, String filename){
		String name = getFileName(filename);
		File file = new File(dir);
		if(file .exists()){
			File ff[] = file.listFiles();
			for(File f : ff){
				if(f.isFile()){
					if(getFileName(f.getName()).equals(name)){
						f.delete();
						return true;
					}
				}
			}
			
		}
		return false;
	}
  	/**
  	 * 递归删除 文件 或者 文件夹 所有
  	 */
	public static boolean delete(String path){
		FileUtil.showDirAsync(path, new Fun<File>() {
			@Override
			public void make(File file) {
				file.delete();
			}
		});
		
		return false;
	}
	/**
	 *  /sdcard/mycc/record/100-101020120120120.amr return amr
	 * @param path
	 * @return
	 */
	public static String getFileType(String path){
			String res = "null";
			if(path == null){
			}else{
				int ii = path.lastIndexOf(".");
				//out(""+ii);
				if(ii >= 0){
					return path.substring(ii+1);
				}
			}
			return res;
		}
	/**
	 * /sdcard/mycc/record/100-101020120120120.amr return asdfa
	 * @param path
	 * @return
	 */
	public static String getFileNameOnly(String path){
			String res = "null";

			if(path == null){
			}else{
					int ii = path.lastIndexOf(".");
					//out(""+ii);
					if(ii >= 0){
						res=path.substring(0,ii); // F:/s/d/1000
						ii = path.lastIndexOf("\\");
						//out(""+ii);
						if(ii >= 0){
							res = res.substring(ii + 1);
						}
					}
			}
			return res;
	}
	/**
	 *  /sdcard/mycc/record/100-101020120120120.amr return asdfa.amr
	 * @param path
	 * @return
	 */
	public static String getFileName(String path){
		String res = "null";

		if(path == null){
		}else{
			int ii = path.lastIndexOf(File.separator);
			//out(""+ii);
			if(ii >= 0){
				res = path.substring(ii + 1);
			}
		}
		return res;
	}
	/**
	 *  /sdcard/mycc/record/100-101020120120120.amr return /sdcard/mycc/record/
	 * @param path
	 * @return
	 */
	public static String getFilePath(String path){
			String res = "";

			if(path == null){
			}else{
				int ii = path.lastIndexOf(File.separator);
				//out(""+ii);
				if(ii >= 0){
					res = path.substring(0, ii );
				}
			}
			return res;
			
		}

	
	
	public static void main(String[] argv){
		String dirpath = "C:\\tomcat\\download";
		String path = "C:\\tomcat\\download\\20180717150340-compareTo2.c";
		

		List<Map> res = (FileUtil.ls(dirpath));
		
		Tools.formatOut(res);
		
		
	}
	public static void out(Object...objects){
//		Tools.out(objects);
	}
	
	
}