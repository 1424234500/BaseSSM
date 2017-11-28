package com.controller;
 

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mode.Page;
import com.service.BaseService;
import com.service.FileService;
import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion.Setting;

import util.FileUtil;
import util.MapListHelp;
import util.MyJson;
import util.SQLHelp;
import util.Tools;
import util.WebHelp;


@Controller
@RequestMapping("/file")
public class FileControll extends BaseControll{
	static public Logger logger = LoggerFactory.getLogger(FileControll.class); 
	@Autowired
	@Qualifier("fileService") 
	protected FileService fileService;
	
	
	@RequestMapping("/list.do")
	public String list(HttpServletRequest request, Map<String,Object> map) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String timefrom = request.getParameter("timefrom");
		String timeto = request.getParameter("timeto");
 
		Page page = Page.getPage(request);
		map.putAll(WebHelp.getRequestMap(request));

		List<String> params = new ArrayList<String>();
		String sql = "select id,(select count(*) from file_down_up where fileid=f.id and type='down') count,name,upuserid,type,file_size(filesize) filesize,to_char(uptime," + SQLHelp.getTimeFormatL() + ") uptime, to_char(changetime," + SQLHelp.getTimeFormatL() + ") changetime,about from fileinfo f where 1=1 ";
		if(Tools.isNull(id)){
			sql += " and id like ? ";
			params.add("%" + id + "%");
		} 
		if(Tools.isNull(name)){
			sql += " and name like ? ";
			params.add("%" + name + "%");
		}
		if(Tools.isNull(timefrom)){
			sql += " and uptime >= " + SQLHelp.to_dateL();
			params.add(timefrom);
		}
		if(Tools.isNull(timeto)){
			sql += " and uptime <= " + SQLHelp.to_dateL();
			params.add( timeto);
		} 
	    List<Map> res = baseService.findPage(page, sql, params.toArray() ); 
	    map.put("res", res); 
		map.put("PAGE", page);
		return "file/list";
	} 
	

	
	@RequestMapping("/delete.do")
	public void delete(HttpServletRequest request,  PrintWriter pw){
		String id = request.getParameter("id");  

		int res = 0;
		String path = baseService.getString("select path from fileinfo where id=?", id);
		res = baseService.executeSql("delete from fileinfo where id=?", id);
		FileUtil.delete(path);
		
	    pw.write("" + res);
	}
	
	@RequestMapping("/update.do")
	public void update(HttpServletRequest request,  PrintWriter pw){
		String id = request.getParameter("id"); 
		String about = request.getParameter("about"); 
	    
		int res = baseService.executeSql("update fileinfo set about=? where id=? ", about, id);
		pw.write("" + res);
	}

	@RequestMapping("/get.do")
	public void get(HttpServletRequest request,  PrintWriter pw){
		String id = request.getParameter("id");  

		Map map = baseService.findOne("select * from fileinfo where id=? ", id );
	    pw.write("" + MyJson.makeJson("obj", map));
	}
	 /**  
     * 文件下载功能  
     * @param request  
     * @param response  
     * @throws Exception  
     */  
    @RequestMapping("/down.do")  
    public void down(HttpServletRequest request,HttpServletResponse response) throws Exception{  
    	long starttime = System.currentTimeMillis();

		String id = request.getParameter("id");  
		String path = baseService.getString("select path from fileinfo where id=?", id);
		String name = baseService.getString("select name from fileinfo where id=?", id);
		
        name = URLEncoder.encode(name,"UTF-8");      //转码，免得文件名中文乱码  
        //设置文件下载头  
        response.addHeader("Content-Disposition", "attachment;filename=" + name);    
        //设置文件ContentType类型，这样设置，会自动判断下载文件类型    
        response.setContentType("multipart/form-data");   
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());  
        InputStream in = null;
        try {    
            // 一次读多个字节  
            byte[] tempbytes = new byte[512];  
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
            log("down file", name, path, Tools.getStringByTime(deta),Tools.getStringBySize(size) );
            fileService.fileUpDown(id, "down", deta+"" ); 

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
        String about = request.getParameter("about");
         
        String name = file.getOriginalFilename();
        String newName = Tools.getTimeSequence() + "-" + name;
        String dir = UtilTools.getDir();
        String path = dir + newName;
        FileOutputStream out = new FileOutputStream(path);
        int res = 0; 
//      fos.write(file.getBytes());
        InputStream in = null;
        try {    
            // 一次读多个字节  
            byte[] tempbytes = new byte[512];  
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
            String key = fileService.upload(getUser(request).getId(), name, path, about); 
            res = key.equals("0")?0:1;
            log("up file", name, path, Tools.getStringByTime(deta) , Tools.getStringBySize(size));

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
		 logger.info(Tools.getString(objs));
	}
    
}