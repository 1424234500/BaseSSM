package com.walker.common.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map工厂类 new MakeLinkMap().put("key","value").build();
 * @author Walker
 * 2017年12月11日15:08:12
 */
public class MakeLinkMap{ 
	Map<String, Object> map;
	public MakeLinkMap(){
		map = new LinkedHashMap<String, Object>(); 
	}
	public MakeLinkMap put(String key, Object value){
		map.put(key, value);
		return this;
	}
	public Map<String, Object> build(){
		return map;
	}
	 
}