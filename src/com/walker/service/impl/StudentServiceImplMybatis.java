package com.walker.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walker.common.util.MapListUtil;
import com.walker.common.util.Page;
import com.walker.common.util.Tools;
import com.walker.core.database.SqlHelp;
import com.walker.service.StudentService;
import com.walker.web.dao.mybatis.BaseMapper;

@Service("studentServiceMybatis")
public class StudentServiceImplMybatis implements StudentService, Serializable {

	private static final long serialVersionUID = 8304941820771045214L;
 
	/**
	 * mybatis入口
	 */
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Autowired
	protected BaseMapper baseMapper;

	@Override
	public List<Map<String, Object>> list(String id, String name, String timefrom, String timeto, Page page) {
		if(!Tools.notNull(name)) name = "";
		else name = SqlHelp.like(name);
		if(!Tools.notNull(id)) id = "";
		else  id = SqlHelp.like(id);
		
		Map map = MapListUtil.getMap()
				.put("name", name)
				.put("id", id)
				.put("timefrom", timefrom)
				.put("timeto", timeto)
				.put("pagestart", page.start())
				.put("pagestop", page.stop())
				.build();  
		page.setNUM(baseMapper.count(map));
		return baseMapper.find(map);
	}

	@Override
	public int update(String id, String name, String time) {
		Map map = MapListUtil.getMap().put("name", name)
				.put("id", id)
				.put("time", time)
				.build(); 
		return baseMapper.update(map);
	}

	@Override
	public int delete(String id) {
		Map map = MapListUtil.getMap() 
				.put("id", id)
				.build();
		return baseMapper.delete(map);
	}

	@Override
	public Map<String, Object> get(String id) {
		Map<String, Object> map = MapListUtil.getMap() 
				.put("id", id)
				.build();
		List<Map<String, Object>> list = baseMapper.find(map);
		Map<String, Object> res = null;
		if(list != null && list.size() > 0){
			res = list.get(0);
		}else{
			res = new HashMap();
		}
		return res;
	}

	@Override
	public int add(String name, String time) {
		Map map = MapListUtil.getMap() 
				.put("name", name)
				.put("time", time)
				.build();
		return baseMapper.add(map);
	}

}