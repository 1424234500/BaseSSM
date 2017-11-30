package com.dao.mybatis;

import java.util.List;
import java.util.Map;


public interface BaseMapper {   
	Long count(Map map);
    List<Map> find(Map map);   

	int update(Map map);
	int delete(Map map);
	int add(Map map);

	
	
	
	
}  