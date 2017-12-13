package com.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.hibernate.BaseDao;
import com.mode.Page;
import com.service.LogService;
import com.service.StudentService;

import util.MakeMap;
import util.MapListHelp;
import util.SQLHelp;
import util.Tools;
import util.database.Redis;
@SuppressWarnings("unchecked")

@Service("logService")
public class LogServiceImpl implements LogService,Serializable {
	private static final long serialVersionUID = 8304941820771045214L;
 
    @Autowired
    protected BaseDao baseDao;
    //info:
    //id,userid,time,url,ip,mac,port,about
	@Override
	public void userMake(String userid, String url, String ip, String host, int port, String params) {
		int res = 0;
		params = Tools.cutString(params, 180);
		res = baseDao.executeSql("insert into info"
				+ "(id,time,userid,url,ip,mac,port,about) "
				+ "values"
				+ "(seq_info.nextval,sysdate,?,?,?,?,?,?) "
				,userid,url,ip,host,port,params
			);
	}
	
	
	
	@Override
	public void exeStatis(String url, String params, long costtime) {
		int res = 0;
		
		Redis redis = Redis.getInstance();
		if(redis.existsMap(url)){ 
			Map map = redis.getMap(url);
			map.put("costtime", Tools.parseLong(MapListHelp.getMap(map, "costtime")) + costtime);
			map.put("count", Tools.parseInt(MapListHelp.getMap(map, "count")) + 1);
			redis.setMap(url, map); 
		}else{
			redis.setMap(url, MapListHelp.map()
					.put("url", url)
					.put("costtime", costtime)
					.put("count", 1)
					.build());
		}
		  
//		res = baseDao.executeSql("insert into log_time"
//				+ "(id, url, count, time, costtime) "
//				+ "values"
//				+ "(seq_log_time.nextval, ?, '1', sysdate, ?) "
//				,url, costtime
//				);
	}



	@Override
	public void saveStatis() { 
		Redis redis = Redis.getInstance();
		Set<String> keys = redis.getKeys();
		for(String key : keys){
			if(redis.existsMap(key)){ 
				Map map = redis.getMap(key); 
				int res = baseDao.executeSql("insert into log_time"
						+ "(id, url, count, time, costtime) "
						+ "values"
						+ "(seq_log_time.nextval, ?, ?, sysdate, ?) "
						,map.get("url"), map.get("count"), map.get("costtime") 
					);
				if(res > 0){
					redis.removeMap(key);
				}
			}
		}
	
		
	}

}