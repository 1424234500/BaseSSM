package com.walker.core.database;

import java.util.List;
import java.util.Map;

import com.walker.common.util.Page;

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
	public List<String> findColumns(String sql);
	public List<String> getColumns(String tableName);

	/**
	 * 获得结果集
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数
	 * @return 结果集
	 */
	public List<Map<String, Object>> find(String sql, Object... params);
	public Map<String, Object> findOne(String sql, Object... params);

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
	public List<Map<String, Object>> findPage(String sql, int page, int rows, Object... params);
	public List<Map<String, Object>> findPage(Page page, String sql, Object... params);

	/**
	 * 执行SQL语句
	 * @return 响应行数
	 */
	public int executeSql(String sql, Object... params);

	/**
	 * 批量执行sql
	 * @return
	 */
	public int[] executeSql(String sql, List<List<Object>> objs);

	/**
	 * 统计
	 * @param sql  SQL语句
	 * @param params  参数
	 * @return 数目
	 */
	public int count(String sql, Object... params);

	
}
