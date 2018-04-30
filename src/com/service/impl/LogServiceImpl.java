package com.service.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dao.Redis;
import com.dao.hibernate.BaseDao;
import com.service.LogService;

import util.MapListUtil;
import util.Tools;

@Service("logService")
@Scope("prototype") 
public class LogServiceImpl implements LogService,Serializable {
	private static final long serialVersionUID = 8304941820771045214L;
	static public Logger logger = LoggerFactory.getLogger(LogServiceImpl.class); 

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
		Redis redis = Redis.getInstance();

		if(redis != null && redis.existsMap(url)){ 
			Map<String, String> map = redis.getMap(url);
			map.put("costtime", (Tools.parseLong(map.get("costtime")) + costtime) + "");
			map.put("count", (Tools.parseInt(map.get("count")) + 1) + "");
			redis.setMap(url, map); 
		}else{
			redis.setMap(url, MapListUtil.map()
					.put("url", url)
					.put("costtime", costtime)
					.put("count", 1)
					.build());
		} 
		//redis.show();
	}


	@Override
	public void saveStatis() { 
		Redis redis = Redis.getInstance();
		//redis.show();
		Set<String> keys = redis.getKeys();
		if(keys != null && !keys.isEmpty())
			for(String key : keys){
				if(redis.existsMap(key)){ 
					Map map = redis.getMap(key); 
					int res = baseDao.executeSql("insert into log_time"
							+ "(id, url, count, time, costtime) "
							+ "values"
							+ "(seq_log_time.nextval, ?, ?, sysdate, ?) "
							,map.get("url"), map.get("count"), map.get("costtime") 
						); 
				}
			}
		redis.clearKeys();
		//redis.show();
	}

}