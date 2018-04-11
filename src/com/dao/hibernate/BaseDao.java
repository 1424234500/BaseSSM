package com.dao.hibernate;

import java.util.List;
import java.util.Map;

/**
 * 基础数据库操作类
 * 
 * 统一使用Map<String, Object>对象
 * 
 * @author
 * 
 */
public interface BaseDao {

	/**
	 * 根据SQL获得结果集列名数组
	 * 
	 * @param sql
	 *            SQL语句
	 * @return String List数组
	 */
	public List findColumns(String sql);

	/**
	 * 获得结果集
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数
	 * @return 结果集
	 */
	public List<Map> find(String sql, Object... params);

	public Map findOne(String sql, Object... params);

	/**
	 * 获得结果集
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数
	 * @param page
	 *            要显示第几页
	 * @param rows
	 *            每页显示多少条
	 * @return 结果集
	 */
	public List<Map> findPage(String sql, int page, int rows, Object... params);

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数
	 * @return 响应行数
	 */
	public int executeSql(String sql, Object... params);

	/**
	 * 统计
	 * @param sql  SQL语句
	 * @param params  参数
	 * @return 数目
	 */
	public Long count(String sql, Object... params);

	/**
	 * 查询结果为单字符串
	 * 
	 * @param sql
	 *            SQL语句
	 * 
	 *            参数
	 * @return String
	 */
	public String getString(String sql, Object... params);

	
	public List<Object> getColumns(String tableName);
	
}
