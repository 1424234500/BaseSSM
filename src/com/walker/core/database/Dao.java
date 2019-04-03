package com.walker.core.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.walker.common.util.Page;

/**
 * 数据库常用操作工具
 * 选择一种连接池实现
 * 每种连接池对应多种数据源 多种数据库
 * 
 * jdbc 
 * 一个实例 绑定 连接池 和 数据源  但 底层 是同一个连接池 且 每个连接池每个 数据源是 唯一 
 */
public class Dao implements BaseDao{
	private static Logger log = Logger.getLogger(Dao.class); 

	private Pool pool;
	private String dsName;
	private Connection conn;
	
	public Dao(){
		this.pool = PoolMgr.getInstance();
	}
	public Dao(Type type){
		this.pool = PoolMgr.getInstance(type);
	}
	public void setDs(String dsName){
		this.dsName = dsName;
	}

	// 获取链接
	private Connection getConnection() throws SQLException {
		if(conn == null || conn.isClosed()) {
			conn = this.pool.getConn(dsName);
		}
		return conn;
	}
	private void close(Connection conn, PreparedStatement pst, ResultSet rs) {
		this.pool.close(conn, pst, rs);
	}
//	PreparedStatement ps = conn.prepareStatement(sql);
//	conn = this.pool.getConn(dsName);
//	this.pool.close(conn, pst, rs);

	private List<Map<String, Object>> rs2list(ResultSet rs) {
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		try {
			ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
			int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
			while (rs.next()) {
				Map<String, Object> map = new HashMap<>(columnCount);
				for (int i = 1; i <= columnCount; i++) {
					map.put(md.getColumnName(i), rs.getObject(i));
				}
				res.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
 
	@Override
	public List<String> findColumns(String sql) {
		List<String> res = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			res = new ArrayList<>(columnCount);
			for(int i=0; i < columnCount; i++){
				res.add(md.getColumnName(i+1));
			}
		} catch (Exception e) {
			log.error(sql, e);
		} finally {
			close(conn, pst, rs);
		}
		return res;
	}
	@Override
	public List<String> getColumns(String tableName) {
		return findColumns("SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = upper('" + tableName + "') ORDER BY COLUMN_ID");
	}
	@Override
	public List<Map<String, Object>> find(String sql, Object... objects) {
		List<Map<String, Object>> res = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			pst = conn.prepareStatement(sql);
			for (int i = 0; i < objects.length; i++) {
				pst.setObject(i + 1, objects[i]); 
				// string int obj 转换与查询问题！？？！
			}
			rs = pst.executeQuery();
			res = rs2list(rs);
		} catch (Exception e) {
			res = new ArrayList<>();
			log.error(SqlHelp.makeSql(sql, objects), e);
		} finally {
			close(conn, pst, rs);
		}
		return res;

	}
	@Override
	public Map<String, Object> findOne(String sql, Object... objects) {
		List<Map<String, Object>> list = this.find("select * from " + sql + " where rownum <= 1 ", objects);
		Map<String, Object> res = null;
		if(list.size() >= 1) {
			res = list.get(0);
		}
		return res;
	}
	@Override
	public List<Map<String, Object>> findPage(String sql, int page, int rows, Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Map<String, Object>> findPage(Page page, String sql, Object... params) {
		page.setNUM(this.count(sql, params ));
		return this.findPage(sql,page.getNOWPAGE(),page.getSHOWNUM(), params );
	}
	
	@Override
	public int executeSql(String sql, Object... objects) {
		int res = 0;
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = this.getConnection();
			pst = conn.prepareStatement(sql);
			for (int i = 0; i < objects.length; i++) {
				pst.setObject(i + 1, objects[i]);
			}
			res = pst.executeUpdate();
		} catch (Exception e) {
			res = -1;
			log.error(SqlHelp.makeSql(sql, objects), e);
		} finally {
			close(conn, pst, null);
		}
		return res;
	}
	public int[] executeSql(String sql, List<List<Object>> objs) {
		int[] res = {};
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = this.getConnection();
			pst = conn.prepareStatement(sql);
			for(List<Object> objects : objs) {
				for (int i = 0; i < objects.size(); i++) {
					pst.setObject(i + 1, objects.get(i));
				}
				pst.addBatch();
			}
			res = pst.executeBatch();
		} catch (Exception e) {
			log.error(sql + " batch size:"+objs.size(), e);
		} finally {
			close(conn, pst, null);
		}
		return res;
	}
	@Override
	public int count(String sql, Object... objects) {
		int res = 0;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			pst = conn.prepareStatement("select count(*) from (" + sql + ")");
			for (int i = 0; i < objects.length; i++) {
				pst.setObject(i + 1, objects[i]); 
			}
			rs = pst.executeQuery();
			while (rs.next()) {
				res = rs.getInt(1); // 取出count(*) 第一个整数
				break;
			}
		} catch (Exception e) {
			log.error(SqlHelp.makeSql(sql, objects), e);
		} finally {
			close(conn, pst, null);
		}
		return res;
	}
}
