package com.dao.hibernate.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dao.hibernate.BaseDao;

import util.Tools;

 
@Repository("baseDao")
public class BaseDaoImpl implements BaseDao  {

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * 获得当前事物的session
	 * 
	 * @return org.hibernate.Session
	 */
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	 
	public List<String> findColumns(String sql){
		final String sqlStr=sql;
		final List<String> list=new ArrayList<String>();
		getCurrentSession().doWork( new Work() {  
		    public void execute(Connection connection) {
		    	try{
		    	PreparedStatement pstm=connection.prepareStatement(sqlStr);
		    	ResultSet rst  = pstm.executeQuery(); 
		    	ResultSetMetaData rsmd=rst.getMetaData();
		    	 for(int i=0;i<rsmd.getColumnCount();i++){
		    		 list.add(rsmd.getColumnName(i+1));
		    	 }
		      }catch(Exception e){
		    	e.printStackTrace();
		    }
		    }  
		    });
		return list;
	}
	  

	@Override
	public List<Map> find(String sql, Object... params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		setParams(q, params);
		return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	@Override
	public Map findOne(String sql, Object... params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		setParams(q, params);
		List<Map> list = q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		Map res = null;
		if(list != null && list.size() > 0){
			res = list.get(0);
		}else{
			res = new HashMap();
		}
		return res;
	}
	static public Logger logger = LoggerFactory.getLogger("Hibernate"); 
	public void out(String str){
		logger.info(str);
	}
	
	public void setParams(SQLQuery q, Object...params){
		if (params != null &&  params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		
		out(Tools.makeSql(q.getQueryString(), params)); 
	}
	@Override
	public List<Map> findPage(String sql, int page, int rows, Object... params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		setParams(q, params);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
 
	@Override
	public int executeSql(String sql, Object... params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		setParams(q, params);
		return q.executeUpdate();
	}
 
	@Override
	public Long count(String sql, Object... params) {
		SQLQuery q = getCurrentSession().createSQLQuery("select count(*) from ("+sql+") ");
		setParams(q, params);
		return ((Number) q.uniqueResult()).longValue();
	}
	
	@Override
	public String getString(String sql, Object... params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		setParams(q, params);
		return   q.uniqueResult().toString();
	}
	

}
