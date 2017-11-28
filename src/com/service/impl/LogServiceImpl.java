package com.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import util.SQLHelp;
import util.Tools;

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
 

}