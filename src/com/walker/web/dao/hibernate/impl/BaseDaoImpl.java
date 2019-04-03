package com.walker.web.dao.hibernate.impl;

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
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.walker.core.database.SqlHelp;
import com.walker.web.dao.hibernate.BaseDao;

 
@Repository("baseDao")
public class BaseDaoImpl implements BaseDao  {
	static public Logger log = Logger.getLogger("Hibernate"); 
	public void out(String str){
		log.debug(str);
	}
	
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * 获得当前事务的session
	 * @return org.hibernate.Session
	 */
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 获取列名集合的第一种方式
	 */
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
	  
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> find(String sql, Object... params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		setParams(q, params);
		return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findOne(String sql, Object... params) {
		SQLQuery q = getCurrentSession().createSQLQuery("select * from (" + sql + ") where rownum <= 1 ");
		setParams(q, params);
		List<Map<String, Object>> list = q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		Map<String, Object> res = null;
		if(list != null && list.size() > 0){
			res = list.get(0);
		}else{
			res = new HashMap<String, Object>();
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findPage(String sql, int page, int rows, Object... params) {
		page = page <= 0 ? 1 : page;
		rows = rows <= 0 ? 2 : rows;
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
	
	@SuppressWarnings("unchecked")
	public List<String> getColumns(String tableName){
		String sql = "";
		//oracle
		sql = "SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = upper('" + tableName + "') ORDER BY COLUMN_ID";
		//sqlserver
		//select name from syscolumns where id=object_id('表名');
		//mysql
		//select COLUMN_NAME from information_schema.columns where table_name='tablename'
		
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		setParams(q);
		List<Map<String, Object>> list = q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		List<String> res = new ArrayList<String>();
		
		if(list != null){
			for(Map<String, Object> map : list){
				res.add(map.get("COLUMN_NAME").toString());
			}
		}
		
		return res;
	}

	
	public void setParams(SQLQuery q, Object...params){
		if (params != null &&  params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		out(SqlHelp.makeSql(q.getQueryString(), params)); 
	}
}
