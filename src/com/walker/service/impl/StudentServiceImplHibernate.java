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
import com.walker.service.StudentService;
import com.walker.web.dao.hibernate.BaseDao;

@Service("studentServiceHibernate")
public class StudentServiceImplHibernate implements StudentService,Serializable {
 
	private static final long serialVersionUID = 8304941820771045214L;
	/**
     * hibernate入口
     */
	@Autowired
	private SessionFactory sessionFactory;
    
    @Autowired
    protected BaseDao baseDao;

	@Override
	public List<Map<String, Object>> list(String id, String name, String timefrom, String timeto, Page page) {
		String sql = "";
		List<String> params = new ArrayList<String>();
		sql += "select id,name,to_char(time,'yyyy-mm-dd hh24:mi:ss') time from student where 1=1 ";
		if(Tools.notNull(id)){
			sql += " and id like ? ";
			params.add("%" + id + "%");
		} 
		if(Tools.notNull(name)){
			sql += " and name like ? ";
			params.add("%" + name + "%");
		}
		if(Tools.notNull(timefrom)){
			sql += " and time >= " + SqlHelp.to_dateL();
			params.add(timefrom);
		}
		if(Tools.notNull(timeto)){
			sql += " and time <= " + SqlHelp.to_dateL();
			params.add( timeto);
		} 
		
		page.setNUM(baseDao.count(sql, params.toArray()));
		return baseDao.findPage(sql,page.getNOWPAGE(),page.getPAGENUM(), params.toArray());
	}

	@Override
	public int update(String id, String name, String time) {
		int res = baseDao.executeSql(
				"update student set name=?,time=to_date(?,'yyyy-mm-dd hh24:mi:ss') where id=?",
				name,time,id  );
		return res;
	}
	@Override
	public int add(String name, String time) {
		int res = 0;
		res = baseDao.executeSql("insert into student values(lpad(SEQ_STUDENT.nextval,4, '0'),?," + SqlHelp.to_dateL() + ")", name, time);
 		return res;
	}
	@Override
	public int delete(String id) {
		int res = 0;
		res = baseDao.executeSql("delete from student where id=? ", id);
 		return res;
	}
	@Override
	public Map get(String id) {
 		return  baseDao.findOne("select id,name,to_char(time,'yyyy-mm-dd hh24:mi:ss') time from student where id=? ", id);
	}

}