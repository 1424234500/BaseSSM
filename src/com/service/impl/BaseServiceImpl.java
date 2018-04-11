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

import com.controller.Page;
import com.dao.hibernate.BaseDao;
import com.service.BaseService;
import com.service.StudentService;

import util.MakeMap;
import util.Tools;
import util.database.SqlHelp;

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
	public List<Map> find(String sql, Object... params) {
		return baseDao.find(sql, params);
	}

	@Override
	public Map findOne(String sql, Object... params) {
		return baseDao.findOne(sql, params);
	}

	@Override
	public List<Map> findPage(Page page, String sql, Object... params) {
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

	@Override
	public String getString(String sql, Object... params) {
		return baseDao.getString(sql, params);
	}

	@Override
	public List<Object> getColumns(String tableName) {
		return baseDao.getColumns(tableName);
	}
 

}