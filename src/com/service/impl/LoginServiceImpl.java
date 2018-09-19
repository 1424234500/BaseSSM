package com.service.impl;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.controller.Context;
import com.dao.Redis;
import com.dao.hibernate.BaseDao;
import com.mode.LoginUser;
import com.service.LogService;
import com.service.LoginService;

import util.Bean;
import util.MD5;
import util.MapListUtil;
import util.Tools;
import util.cache.Cache;
import util.cache.CacheMgr;

@Service("loginService")
@Scope("prototype") 
public class LoginServiceImpl implements LoginService,Serializable {
	private static final long serialVersionUID = 8304941820771045214L;
    @Autowired
    private BaseDao baseDao;    

	private Cache<String> cache = CacheMgr.getInstance();

	@Override
	public Boolean login() {
		return login("test", "");
	}

	@Override
	public Boolean login(String id, String pwd) {
		String token = MD5.makeKey(id, System.currentTimeMillis());
		Map map = cache.get(CACHE_KEY, new LinkedHashMap<String, Object>());
		Bean bean = new Bean().put("token", token).put("id", id).put("time", System.currentTimeMillis()).put("expire", 60L * 1000);
		map.put(token, bean);
		cache.put(CACHE_KEY, map);	
		logger.info("登录" + id + "." + pwd + "." + token);
		Context.getRequest().getSession().setAttribute("token", token);
		return true;
	}
    



}