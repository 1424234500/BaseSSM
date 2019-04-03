package com.walker.service;

import java.util.List;
import java.util.Map;

import com.walker.common.util.Page;

/**
 * Hibernate专用
 * 基础数据库操作类 负责把上级传递的参数拼接sql递交baseDao查询
 * 统一使用Map<String, Object>对象
 * 参数都是Object...objs[] 占位符方式
 * 相比baseDao只在findPage时做了Page转换为页码条数处理
 * 使用baseService相比BaseDao加以事务管理
 */
public interface BaseService {

	/**
	 * 查询列名字集合List<String>
	 */
	public List<String> findColumns(String sql);
	public List<String> getColumns(String tableName);

	/**
	 * 查询结果集合
	 */
	public List<Map<String, Object>> find(String sql, Object... params);
	/**
	 * 查询一条记录 往往根据id来查询 此处只做取一条处理
	 */
	public Map<String, Object> findOne(String sql, Object... params);

	/**
	 * 查询分页结果集合
	 */
	public List<Map<String, Object>> findPage(Page page, String sql, Object... params);

	/**
	 * 执行sql语句
	 */
	public int executeSql(String sql, Object... params);

	/**
	 * 查询出来数据的条数
	 */
	public Long count(String sql, Object... params);


}
