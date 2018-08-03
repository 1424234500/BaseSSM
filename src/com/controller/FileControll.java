package com.controller;
 

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.service.FileService;

import util.Bean;
import util.FileUtil;
import util.MapListUtil;
import util.JsonUtil;
import util.Tools;
import util.database.SqlHelp; 


@Controller
@RequestMapping("/file")
public class FileControll extends BaseControll{
	public FileControll() {
		super(FileControll.class, "");
	}

	static public Logger logger = LoggerFactory.getLogger(FileControll.class); 
	@Autowired
	@Qualifier("fileService") 
	protected FileService fileService;
	
	static int cacheSize = 512;
	
	@RequestMapping("/fileCols.do")
	public void fileCols(HttpServletRequest request, HttpServletResponse response) throws IOException {
		writeJson(response, FileUtil.getFileMap());
	}
	@RequestMapping("/fileDir.do")
	public void fileDir(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String dir = request.getParameter("dir");
		String name = request.getParameter("name");
		String newdir = request.getParameter("newdir");
		

		if( ! Tools.notNull(dir)){
			dir = UtilTools.getUploadDir();
		}
		if( Tools.notNull(newdir, name)){
			FileUtil.mkdir(dir + File.separator + name);
		}
		int type = FileUtil.check(dir);
		if(type == 1){ //若是文件夹
			writeJson(response, FileUtil.ls(dir));
		}else if(type == 0){ //文件
			this.down(request, response);
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
	    writeJson(response, res, page);
	} 
	

	
	@RequestMapping("/delete.do")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException { 
		String path = request.getParameter("PATH");  
		int count = 0;
		String info = "";
		if(Tools.notNull(path)){
			if(path.indexOf(UtilTools.getUploadDir()) == 0){
				count = baseService.executeSql("delete from fileinfo where path=?", path);
				FileUtil.delete(path);
			}else{
				info = "无权限删除" + path;
			}
		}else{
			info = "路径为null";
		}
		
	    Map res = MapListUtil.getMap().put("res", count).put("info",  info).build();
		writeJson(response, res);
	}
	
	@RequestMapping("/update.do")
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException { 
		String id = request.getParameter("PATH"); 
		String about = request.getParameter("ABOUT"); 
	    
		int count = baseService.executeSql("update fileinfo set about=? where PATH=? ", about, id);
		Map res = MapListUtil.getMap().put("res", count).build();
		writeJson(response, res);	
	}

	@RequestMapping("/get.do")
	public void get(HttpServletRequest request, HttpServletResponse response) throws IOException { 
		String path = request.getParameter("PATH");  
		Map map = FileUtil.getFileMap(path);
		writeJson(response, map);	
	}
	 /**  
     * 文件下载功能  
     * @param request  
     * @param response  
     * @throws Exception  
     */  
    @RequestMapping("/download.do")  
    public void down(HttpServletRequest request,HttpServletResponse response) throws Exception{  
    	long starttime = System.currentTimeMillis();

		String path = request.getParameter("path");
		if(path == null || FileUtil.check(path) != 0){
			return;
		}
			
		String name = FileUtil.getFileName(path);
        name = URLEncoder.encode(name,"UTF-8");      //转码，免得文件名中文乱码  
        //设置文件下载头  
        response.addHeader("Content-Disposition", "attachment;filename=" + name);    
        //设置文件ContentType类型，这样设置，会自动判断下载文件类型    
        response.setContentType("multipart/form-data");   
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
            	catch (Exception e1) {  } 
            }  
            if (out != null) { 
            	try {  out.close();   }
            	catch (Exception e1) {  } 
            }  
        }  
        
       
    }  
	@RequestMapping(value="/upload.do",method=RequestMethod.POST)
    public void upload(HttpServletRequest request,  PrintWriter pw) throws IOException{
    	long starttime = System.currentTimeMillis();

        MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
        MultipartFile file = mreq.getFile("file");
        String uppath = request.getParameter("path");
        String name = file.getOriginalFilename();
        String newName = name; // Tools.getTimeSequence() + "-" + 
        String dir = Tools.notNull(uppath) ? uppath : UtilTools.getUploadDir();
        String path = dir + File.separator + newName;
        FileOutputStream out = new FileOutputStream(path);
        int res = 0; 
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
            String key = fileService.upload(getUser(request).getId(), name, path, ""); 
            res = key.equals("0")?0:1;
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
        
        pw.write("" + res);
    }
	
	
	@Override
	public void log(Object... objs) {
		 logger.info(Tools.objects2string(objs));
	}
    
}