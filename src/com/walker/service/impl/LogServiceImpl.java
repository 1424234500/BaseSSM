package com.walker.service.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.walker.common.util.Bean;
import com.walker.common.util.MapListUtil;
import com.walker.common.util.Tools;
import com.walker.core.cache.Cache;
import com.walker.core.cache.CacheMgr;
import com.walker.service.LogService;
import com.walker.web.dao.Redis;
import com.walker.web.dao.hibernate.BaseDao;

@Service("logService")
@Scope("prototype") 
public class LogServiceImpl implements LogService,Serializable {
	private static final long serialVersionUID = 8304941820771045214L;
    @Autowired
    private BaseDao baseDao;    

	private Cache<String> cache = CacheMgr.getInstance();
    
    
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
	
	
	/**
	 * 切换 线程安全的计数方案
	 */
	@Override
	public void exeStatis(String url, String params, long costtime) {
		url = url.split("\\.")[0]; //url编码
		Bean bean = cache.get(CACHE_KEY);
		if(bean != null){
		}else{
			bean = new Bean();
		}
		Bean beanUrl = bean.get(url, new Bean());
		beanUrl.put("url", url);
		beanUrl.put("costtime", bean.get("costtime", 0L) + costtime);
		beanUrl.put("count", bean.get("count", 0) + 1);
		
		bean.put(url, beanUrl);
		cache.put(CACHE_KEY, bean);

	}


	@Override
	public void saveStatis() { 
		Bean bean = cache.get(CACHE_KEY, new Bean());
//		Redis redis = Redis.getInstance();
		//redis.show();
		Set keys = bean.keySet();
		if(keys != null)
			for(Object key : keys){
				if(bean.containsKey(key)){ 
					Bean map = bean.get(key, new Bean()); 
					int res = baseDao.executeSql("insert into log_time"
							+ "(id, url, count, time, costtime) "
							+ "values"
							+ "(seq_log_time.nextval, ?, ?, sysdate, ?) "
							,map.get("url") + ".do", map.get("count"), map.get("costtime") 
						); 
				}
			}
		cache.remove(CACHE_KEY);
//		redis.clearKeys();
		//redis.show();
	}

}