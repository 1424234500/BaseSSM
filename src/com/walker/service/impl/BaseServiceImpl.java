package com.walker.service.impl;

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

import com.walker.common.util.MakeMap;
import com.walker.common.util.Page;
import com.walker.common.util.Tools;
import com.walker.core.database.SqlHelp;
import com.walker.service.BaseService;
import com.walker.service.StudentService;
import com.walker.web.dao.hibernate.BaseDao;

@Service("baseService")
public class BaseServiceImpl implements BaseService,Serializable {
 
	private static final long serialVersionUID = 8304941820771045214L;
	/**
     * hibernate入口
     */
	@Autowired
	private SessionFactory sessionFactory;
    
    @Autowired
    protected BaseDao baseDao;

	@Override
	public List findColumns(String sql) {
		return baseDao.findColumns(sql);
	}
	@Override
	public List<String> getColumns(String tableName) {
		return baseDao.getColumns(tableName);
	}


	@Override
	public List<Map<String, Object>> find(String sql, Object... params) {
		return baseDao.find(sql, params);
	}

	@Override
	public Map findOne(String sql, Object... params) {
		return baseDao.findOne(sql, params);
	}

	@Override
	public List<Map<String, Object>> findPage(Page page, String sql, Object... params) {
		page.setNUM(baseDao.count(sql, params ));
		return baseDao.findPage(sql,page.getNOWPAGE(),page.getSHOWNUM(), params );
	}

	@Override
	public int executeSql(String sql, Object... params) {
		return baseDao.executeSql(sql, params);
	}

	@Override
	public Long count(String sql, Object... params) {
		return baseDao.count(sql, params);
	}

 

}