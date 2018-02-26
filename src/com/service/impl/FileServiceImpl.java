package com.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.controller.UtilTools;
import com.dao.hibernate.BaseDao;
import com.service.FileService;

import util.FileUtil;
import util.Fun;
import util.MapListHelp;
import util.SQLHelp;
import util.Tools;

@Service("fileService")
public class FileServiceImpl implements FileService,Serializable {
	private static final long serialVersionUID = 8304941820771045214L;
	static public Logger logger = LoggerFactory.getLogger("File"); 

    @Autowired
    protected BaseDao baseDao;
    //info:
//文件管理 fileinfo: id,name,upuserid,filesize,type,path,uptime,createtime,changetime,about

    
	@Override
	public void initDirs() {
		FileUtil.mkdir( UtilTools.getUploadDir());
	}
    
	@Override
	public void scan() {
		//删除表中中不存在文件的记录
		final List<Map> list = baseDao.find("select * from fileinfo");
		for(int i = 0; i < list.size(); i++){
			File file = new File(MapListHelp.getList(list, i, "PATH"));
			if(!file.exists() || file.isDirectory()){
				baseDao.executeSql("delete from fileinfo where id=?", MapListHelp.getList(list, i, "ID"));
			}
		} 
		//添加其它文件到表中
		List<File> lf = FileUtil.showDirAsync(UtilTools.getScanDirs(), new Fun<File>() {
			@Override
			public void make(File obj) {
				if(obj.isFile()){
					int flag = 0;
					for(int i = 0; i < list.size(); i++){
						if( obj.getPath().equals(MapListHelp.getList(list, i, "PATH"))){
							flag = 1;	//已存在该文件的记录
							break;
						} 
					}
					if(flag == 0){
						String name = obj.getName();
						String filesize = ""+obj.length();
						String type = FileUtil.getFileType(name);
						String path = obj.getPath();
						String changetime = Tools.dateFormatL(new Date(obj.lastModified()));
						String about = "扫描出";
						baseDao.executeSql("insert into fileinfo"
								+ "(id,                   uptime, name,filesize,  type,path,changetime               ,about ) values "
								+ "(SEQ_fileinfo.Nextval, sysdate,?    ,?      ,  ?   ,?    ,"+ SQLHelp.to_dateL() +",?   ) "
								                                 ,name ,filesize ,type,path,changetime               ,about    );
						logger.info("添加文件记录：" + name);
					}
				}
			}
		});  

	}

	@Override
	public String upload(String id, String name, String path, String about) {
		File file = new File(path);
		String filesize = ""+file.length();
		String type = FileUtil.getFileType(name);
		String changetime = Tools.dateFormatL(new Date(file.lastModified()));
		about = Tools.cutString(about, 500);
		String key = baseDao.getString("select SEQ_fileinfo.Nextval from dual");
		int res = baseDao.executeSql("insert into fileinfo"
				+ "(id,                   uptime, name,filesize,  type,path,changetime               ,about,upuserid ) values "
				+ "(?, sysdate,?    ,?      ,  ?   ,?    ,"+ SQLHelp.to_dateL() +",?, ?   ) "
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