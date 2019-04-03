package com.walker.core.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import com.walker.common.util.ClassUtil;
import com.walker.core.cache.Cache;
import com.walker.core.cache.CacheMgr;

/**
 * 连接接口
 * 抽象出 共用的 配置读取加载
 * @author walker
 *
 */
abstract class Pool {
	/**
	 * 配置的jdbc集合配置
	 * {
	 * oracle : {
	 * 				DriverClass : xxxx
	 * 				JdbcUrl : xxxx
	 * 			},
	 * mysql  : {
	 * 				DriverClass : xxxx
	 * 				JdbcUrl	: xxxx
	 * 			},
	 * }
	 */
	protected static LinkedHashMap<String, Object> dsConfigPro;
	/**
	 * {
	 * 通用数据源配置
	 * initialSize : 9,
	 * maxActive : 32
	 * }
	 */
	protected static LinkedHashMap<String, String> dsConfig;
	
	/**
	 * 默认第一个作为默认数据源
	 */
	protected static String defaultDsName;
	static {
		Cache<String> cache = CacheMgr.getInstance();
		Map<String, Object> jdbc = cache.get("jdbc");
		if(jdbc == null || jdbc.size() == 0){
			throw new RuntimeException("jdbc数据源配置异常 未配置?");
		}
		Map<String, String> ds = cache.get("ds");
		
		dsConfigPro = new LinkedHashMap<>(jdbc);
		dsConfig = new LinkedHashMap<>(ds);
		defaultDsName = cache.get("jdbcdefault", "oracle");
	}
	
	/**
	 * 把map中的key-value作为参数
	 * 反射调用obj中的对应的
	 * setKey(value)
	 */
	protected static void setDataSource(Object dataSource, Map<String, String> map){
		for(String item : map.keySet()){
			//DriverClass=oracle.jdbc.driver.OracleDriver
			//----------->>>>>>>>>>
			//obj.setDriverClass("oracle.jdbc.driver.OracleDriver")
			ClassUtil.doClassMethod(dataSource, "set" + item, map.get(item));
		}
	}
	
	/**
	 * 获取默认数据源 连接
	 */
	abstract Connection getConn();
	/**
	 * 获取指定数据源连接
	 */
	abstract Connection getConn(String dataSource);
	
	abstract void close(Connection conn, PreparedStatement pst, ResultSet rs);
	
	
}
