package com.walker.web.controller;
 

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.walker.common.util.FileUtil;
import com.walker.common.util.MapListUtil;
import com.walker.common.util.Page;
import com.walker.common.util.Tools;
import com.walker.core.database.SqlHelp;
import com.walker.service.FileService;
import com.walker.web.RequestUtil; 


@Controller
@RequestMapping("/file")
public class FileControll extends BaseControll{
	public FileControll() {
		super(FileControll.class, "");
	}

	private static Logger log = Logger.getLogger(FileControll.class); 
	@Autowired
	@Qualifier("fileService") 
	protected FileService fileService;
	
	static int cacheSize = 4096;
	
	@RequestMapping("/fileCols.do")
	public void fileCols(HttpServletRequest request, HttpServletResponse response) throws IOException {
		echo(FileUtil.getFileMap());
	}
	@RequestMapping("/fileDir.do")
	public void fileDir(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String dir = request.getParameter("dir");
		String name = request.getParameter("name");
		String newdir = request.getParameter("newdir");
		

		if( ! Tools.notNull(dir)){
			dir = Context.getUploadDir();
		}
		if( Tools.notNull(newdir, name)){
			FileUtil.mkdir(dir + File.separator + name);
		}
		int type = FileUtil.check(dir);
		if(type == 1){ //若是文件夹
			echo( FileUtil.ls(dir));
		}else if(type == 0){ //文件
			this.download(request, response);
		}
	} 
	
	
	@RequestMapping("/list.do")
	public void list(HttpServletRequest request, HttpServletResponse response) throws IOException { 
		String id = request.getParameter("ID");
		String name = request.getParameter("NAME");
		String timefrom = request.getParameter("TIMEFROM");
		String timeto = request.getParameter("TIMETO");
 
		Page page = Page.getPage(request);

		List<String> params = new ArrayList<String>();
		String sql = "select id,(select count(*) from file_down_up where fileid=f.id and type='down') count,name,upuserid,type,file_size(filesize) filesize,to_char(uptime," + SqlHelp.getTimeFormatL() + ") uptime, to_char(changetime," + SqlHelp.getTimeFormatL() + ") changetime,about from fileinfo f where 1=1 ";
		if(Tools.notNull(id)){
			sql += " and id like ? ";
			params.add("%" + id + "%");
		} 
		if(Tools.notNull(name)){
			sql += " and name like ? ";
			params.add("%" + name + "%");
		}
		if(Tools.notNull(timefrom)){
			sql += " and uptime >= " + SqlHelp.to_dateL();
			params.add(timefrom);
		}
		if(Tools.notNull(timeto)){
			sql += " and uptime <= " + SqlHelp.to_dateL();
			params.add( timeto);
		} 
	    List<Map<String, Object>> res = baseService.findPage(page, sql, params.toArray() );
	    log(res, page);
	    echo( res, page);
	} 
	

	
	@RequestMapping("/delete.do")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException { 
		String path = request.getParameter("PATH");  
		int count = 0;
		String info = "";
		if(Tools.notNull(path)){
			if(path.startsWith(Context.getUploadDir())){
//				count = baseService.executeSql("delete from fileinfo where path=?", path);
				FileUtil.delete(path);
			}else{
				info = "无修改权限" + path;
			}
		}else{
			info = "路径为null";
		}
		
		echo(info.length()==0, info, count);
	}
	@RequestMapping("/update.do")
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException { 
		String path = request.getParameter("PATH");  //新路径 
		String oldPath = request.getParameter("OLDPATH");   //全路径 path/file
		String oldName = request.getParameter("OLDNAME");  
		String name = request.getParameter("NAME");  //新名字
		
		int count = 0;
		String info = "";
		if(Tools.notNull(path)){
			if((path+File.separator).startsWith(Context.getUploadDir()) 
					&& (oldPath).startsWith(Context.getUploadDir())
					&& Tools.notNull(oldPath, path)){
				FileUtil.mv(oldPath, path + File.separator + name);
			}else{
				info = "无修改权限" + path;
			}
		}else{
			info = "路径为null";
		}
		echo(info.length()==0, info, count);
	}
	
	@RequestMapping("/updatetable.do")
	public void updateTable(HttpServletRequest request, HttpServletResponse response) throws IOException { 
		String id = request.getParameter("PATH"); 
		String about = request.getParameter("ABOUT"); 
	    
		int count = baseService.executeSql("update fileinfo set about=? where PATH=? ", about, id);
		Map res = MapListUtil.getMap().put("res", count).build();
		echo( res);	
	}

	@RequestMapping("/get.do")
	public void get(HttpServletRequest request, HttpServletResponse response) throws IOException { 
		String path = request.getParameter("PATH");  
		Map map = FileUtil.getFileMap(path);
		echo( map);	
	}
	 /**  
     * 文件下载
     * path 若有则按照path下载
     * key  则按照key 映射path下载
     * 
     */  
    @RequestMapping("/download.do")  
    public void download(HttpServletRequest request,HttpServletResponse response) throws Exception{  
    	StopWatch sw = new StopWatch(); 	//耗时监控分析工具
    	sw.start();
    	long starttime = sw.getStartTime();//System.currentTimeMillis();
    	String path = getValue(request, "path");
    	String key = getValue(request, "key");
    	
//		String path1 = new String(path.getBytes("iso-8859-1"), "gbk");
//		String path3 = URLDecoder.decode(path, "utf-8");
//		String path4 = URLDecoder.decode(path);
		path = new String(path.getBytes("iso-8859-1"), "utf-8");
		Boolean res = false;
		String info = "";
		if(key.length() > 0){ //key 映射 path 方式
			Map<String, Object> map = baseService.findOne("select * from fileinfo where id=?", key);
			path = MapListUtil.getMap(map, "ID", "");
		}
		if(path.length() > 0){ //处理path分析文件
			int type = FileUtil.check(path);
			if(type == 1){
				info = path + " 是文件夹";
			}else if(type == 0){
				info = path + " 存在";
				res = true;
			}else{
				info = path + " 不存在";
			}
		}
		if(!res){
			echo(res, info);
			sw.stop();
			return;
		}
		
		String name = FileUtil.getFileName(path);
        RequestUtil.setDownFileName(request, response, name);
        
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());  
        InputStream in = null;
        try {    
            // 一次读多个字节  
            byte[] tempbytes = new byte[cacheSize];  
            long size = 0;
            int len = 0;   
            in = new FileInputStream(new File(path));   
            // 读入多个字节到字节数组中，len为一次读入的字节数  
            while ((len = in.read(tempbytes)) != -1) {  
            	out.write(tempbytes, 0, len);
            	size += len;
            	out.flush();
            }  
            sw.stop();
            log.warn(sw.toString());
            long endtime = System.currentTimeMillis();
            long deta = endtime - starttime;//下载写入耗时deta 大小size 名字name 路径path
            //记录文件上传下载情况 并打印
            // id,fileid,type(up/down),costtime(ms),time
            log("down file", name, path, Tools.calcTime(deta),Tools.calcSize(size) );
//            fileService.fileUpDown(path, "down", deta+"" ); 

        } catch (Exception e1) {  
            e1.printStackTrace();  
        } finally {  
            if (in != null) { 
            	try {  in.close(); }
            	catch (Exception e1) { e1.printStackTrace();  } 
            }  
            if (out != null) { 
            	try {  out.close();   }
            	catch (Exception e1) { e1.printStackTrace(); } 
            }  
        }  
       
    }  
    
    /**
     * 上传文件
     * 存入文件系统 path路径
     * 
     * 生产key 存入数据库映射路径和key
     * 序列 返回key
     * 
     */
	@RequestMapping(value="/upload.do",method=RequestMethod.POST)
    public void upload(HttpServletRequest request,  PrintWriter pw) throws IOException{
		long starttime = System.currentTimeMillis();

        MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
        MultipartFile file = mreq.getFile("file");
        String uppath = request.getParameter("path");

		if(uppath.indexOf(Context.getUploadDir()) != 0){
			echo(false, "无修改权限" + uppath);
			return;
		}
        
        String name = file.getOriginalFilename();
        String newName = name; // Tools.getTimeSequence() + "-" + 
        String dir = Tools.notNull(uppath) ? uppath : Context.getUploadDir();
        String path = dir + File.separator + newName;
        FileOutputStream out = new FileOutputStream(path);
        boolean res = false; 
        String key = "";
//      fos.write(file.getBytes());
        InputStream in = null;
        try {    
            // 一次读多个字节  
            byte[] tempbytes = new byte[cacheSize];  
            long size = 0;
            int len = 0;  
            in = file.getInputStream();  
            // 读入多个字节到字节数组中，len为一次读入的字节数  
            while ((len = in.read(tempbytes)) != -1) {  
            	out.write(tempbytes, 0, len);
            	size += len;
            	out.flush();
            }  
            long endtime = System.currentTimeMillis();
            long deta = endtime - starttime;//下载写入耗时deta 大小size 名字name 路径path
            //记录文件上传下载情况 并打印
            // id,fileid,type(up/down),costtime(ms),time
            key = fileService.upload(getUser().getId(), name, path, ""); 
            res = key.equals("0");

            log("up file", name, path, Tools.calcTime(deta) , Tools.calcSize(size));
            fileService.fileUpDown(key, "up", deta+""); 
        } catch (Exception e1) {  
            e1.printStackTrace();  
        } finally {  
            if (in != null) { 
            	try {  in.close(); }
            	catch (Exception e1) {  } 
            }  
            if (out != null) { 
            	try {  out.close();   }
            	catch (Exception e1) {  } 
            }  
        }  
        
        echo(res, key, path);
    }
	
	
	@Override
	public void log(Object... objs) {
		 log.info(Tools.objects2string(objs));
	}
    
}