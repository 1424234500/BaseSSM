package com.walker.web.dao.mybatis;

import java.util.List;
import java.util.Map;


public interface BaseMapper {   
	Long count(Map<String, Object> map);
    List<Map<String, Object>> find(Map<String, Object> map);   

	int update(Map<String, Object> map);
	int delete(Map<String, Object> map);
	int add(Map<String, Object> map);

	
	
	
	
}  