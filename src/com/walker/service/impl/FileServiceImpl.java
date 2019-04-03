package com.walker.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walker.common.util.FileUtil;
import com.walker.common.util.LangUtil;
import com.walker.common.util.MapListUtil;
import com.walker.common.util.Page;
import com.walker.common.util.Tools;
import com.walker.core.database.SqlHelp;
import com.walker.service.FileService;
import com.walker.web.controller.Context;
import com.walker.web.dao.hibernate.BaseDao;

@Service("fileService")
public class FileServiceImpl implements FileService,Serializable {
	private static final long serialVersionUID = 8304941820771045214L;
	private static Logger log = Logger.getLogger("File");
	
	
    @Autowired
    protected BaseDao baseDao;
    //info:
//文件管理 fileinfo: id,name,upuserid,filesize,type,path,uptime,createtime,changetime,about

    
	@Override
	public void initDirs() {
		log.info("** 初始化项目相关文件夹");
		FileUtil.mkdir( Context.getUploadDir());
		log.info("**! 初始化项目相关文件夹");
	}
    
	@Override
	public void scan() {
		//删除表中中不存在文件的记录 删除失效文件
		//添加分页循环处理
		
		Long count = baseDao.count("select * from fileinfo");
		int once = Context.getDbOnce();
		Page pageBean = new Page(once, count);
		int page = pageBean.getPAGENUM();
		while(page > 0){
			List<Map<String, Object>> list = baseDao.findPage("select * from fileinfo", page--, once);
			
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < list.size(); i++){
				String path = MapListUtil.getList(list, i, "PATH");
				File file = new File(path);
				if(!file.exists() || file.isDirectory() ){ //删除不存在的 或者文件夹
					sb.append("'").append(path).append("'").append(",");
				}
			} 
			if(sb.length() > 0){
				sb.setLength(sb.length() - 1);
				baseDao.executeSql("delete from fileinfo where PATH in (" + sb.toString() + ") ");
			}

		}
		//添加其它文件到表中 策略变更 不再扫描文件加入数据库
		/*
		 	List<File> lf = FileUtil.showDirAsync(Context.getScanDirs(), new Fun<File>() {
			@Override
			public Object make(File obj) {
				if(obj.isFile()){
					int flag = 0;
					for(int i = 0; i < list.size(); i++){
						if( obj.getPath().equals(MapListUtil.getList(list, i, "PATH"))){
							flag = 1;	//已存在该文件的记录
							break;
						} 
					}
					if(flag == 0){
						String name = obj.getName();
						String filesize = ""+obj.length();
						String type = FileUtil.getFileType(name);
						String path = obj.getPath();
						String changetime = Tools.formatL(new Date(obj.lastModified()));
						String about = path;
						baseDao.executeSql("insert into fileinfo"
								+ "(id,                   uptime, name,filesize,  type,path,changetime               ,about ) values "
								+ "(SEQ_fileinfo.Nextval, sysdate,?    ,?      ,  ?   ,?    ,"+ SqlHelp.to_dateL() +",?   ) "
								                                 ,name ,filesize ,type,path,changetime               ,about    );
						logger.info("添加文件记录：" + name);
					}
				}
				return obj;
			}
		});  
		*/

	}

	@Override
	public String upload(String id, String name, String path, String about) {
		File file = new File(path);
		String filesize = ""+file.length();
		String type = FileUtil.getFileType(name);
		String changetime = Tools.formatL(new Date(file.lastModified()));
		about = Tools.cutString(about, 500);
//		String key = baseDao.getString("select SEQ_fileinfo.Nextval from dual");
		String key = LangUtil.getUUID();
		int res = baseDao.executeSql("insert into fileinfo"
				+ "(id,                   uptime, name,filesize,  type,path,changetime               ,about,upuserid ) values "
				+ "(?, sysdate,?    ,?      ,  ?   ,?    ,"+ SqlHelp.to_dateL() +",?, ?   ) "
				                                 , key, name ,filesize ,type,path,changetime               ,about, id   );
		if(res == 1){
			return key;
		}
		return res + "";
	}

	@Override
	public int fileUpDown(String fileId, String type, String detaTime) {
        // id,fileid,type(up/down),costtime(ms),time
		return baseDao.executeSql("insert into file_down_up"
				+ "(id, fileid, type, costtime, time)"
				+" values "
				+" (SEQ_file_down_up.Nextval, ?, ?, ?, sysdate) "
				,fileId, type, detaTime);
		
		
		
	}
    
 

}