package util.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Tools;


/**
 * 数据库常用操作工具
 * 选择一种连接池实现
 * 每种连接池对应多种数据源 多种数据库
 * 
 * 一个实例 绑定 连接池 和 数据源  但 底层 是同一个连接池 且 每个连接池每个 数据源是 唯一 
 */
public class Dao {
	private Pool pool;
	private String dsName;
	private Connection conn;
	private Map<String, PreparedStatement> psIndex = new HashMap<>();
	
	private Boolean boolAutoClose = true;
	public Dao(){
		this.pool = PoolFactory.getInstance();
	}
	public Dao(TypePool type){
		this.pool = PoolFactory.getInstance(type);
	}
	public void setDs(String dsName){
		this.dsName = dsName;
	}
	/**
	 * 默认自动关闭连接conn 自动关闭 prepare
	 * 批量操作时最好设置false 手动close conn和prepare
	 */
	public void setAutoClose(Boolean bool){
		this.boolAutoClose = bool;
	}
	
	// 获取链接
	private Connection getConnection() throws SQLException {
		if(conn == null || conn.isClosed())
			conn = pool.getConn(dsName);
		return conn;
	}
	// 获取
	private PreparedStatement getPrepareStatement(Connection conn, String sql) throws SQLException {
		PreparedStatement ps = psIndex.get(sql);
		if(ps == null){
			ps = conn.prepareStatement(sql);
			psIndex.put(sql, ps);
		}
		return ps;
	}

	private void out(Object...objects) {
		if (false) {
			Tools.out(this, objects);
		}
	}
	private void except(Object...objects) {
		Tools.out(this, objects);
	}
	// 得到list数据
	public List<Map<String, Object>> queryList(String sql, Object... objects) {
		List<Map<String, Object>> res = null;
		ResultSet resultSet = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = this.getConnection();
			preparedStatement = this.getPrepareStatement(conn, sql);

			for (int i = 0; i < objects.length; i++) {
				preparedStatement.setObject(i + 1, objects[i]); 
				// string int obj 转换与查询问题！？？！
			}
			resultSet = preparedStatement.executeQuery();
			res = rs2list(resultSet);
		} catch (Exception e) {
			except(sql, objects, e.toString());
		} finally {
			close(resultSet, preparedStatement, conn);
		}
		return res;
	}

	private List<Map<String, Object>> rs2list(ResultSet resultSet) {
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		try {
			ResultSetMetaData md = resultSet.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
			int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(md.getColumnName(i), resultSet.getObject(i));
				}
				res.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 查询一条数据
	 */
	public Map<String, Object> queryOne(String sql, Object... objects) {
		sql = " select * from ( " + sql + " ) where rownum <= 1 ";
		List<Map<String, Object>> res = queryList(sql, objects);
		if (res != null) {
			if (res.size() >= 1) {
				return res.get(0);
			}
		}
		return null;
	}

	public int execSQL(String sql, Object... objects) {
		int res = -1;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPrepareStatement(conn, sql);

			for (int i = 0; i < objects.length; i++) {
				preparedStatement.setObject(i + 1, objects[i]);
			}
			res = preparedStatement.executeUpdate();

			out("execute = " + sql + " args:" + Tools.objects2string(objects));
		} catch (Exception e) {
			except(sql, objects, e.toString());
		} finally {
			// 5. 关闭数据库资源: 由里向外关闭.
			close(null, preparedStatement, connection);
		}
		return res;
	}

	/**
	 * 释放数据库资源的方法
	 * 若是不自动关闭则 只关闭resultSet
	 * 缓存conn和prepareStatement
	 */
	private void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
		if(this.boolAutoClose){
			for(String key : psIndex.keySet()){
				this.pool.close(null, psIndex.get(key), null);
			}
			this.psIndex.clear();

			this.pool.close(connection, preparedStatement, resultSet);
			this.conn = null;
			
		}else{
			this.pool.close(null, null, resultSet);
		}
	}
	/**
	 * 关闭所有缓存的prepareStatement 关闭当前打开的conn
	 */
	public void close(){
		for(String key : psIndex.keySet()){
			this.pool.close(null, psIndex.get(key), null);
		}
		this.psIndex.clear();
		this.pool.close(this.conn, null, null);
	}

	/**
	 * 得到当前数据库 的 当前表的 列名=id/ID 的 大于0的 当前表中并不存在的 最小的 整形的 id号 转为的 字符串
	 */
	public String getAId(String tableName, String columnName, int startI) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int i = startI;
		boolean flag = true;
		try {
			connection = getConnection();
			// 疑问：表名无法用？号占位！？
			preparedStatement = connection.prepareStatement("select  " + columnName + "  from   " + tableName,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
				while (true) {
					resultSet.first();
					flag = true;
					do {
						if (resultSet.getString(1).equals("" + i)) {
							flag = false;
							break;
						}
					} while (resultSet.next());
					if (flag) {
						out("id:" + i);
						break;
					}
					i++;
				}
		} catch (SQLException e) {
			except(tableName, columnName, startI, e.toString());
			return "-1";
		} finally {
			close(resultSet, preparedStatement, connection);
		}
		return "" + i;
	}

	// 查询记录条数专用
	public int executeCount(String sql, Object... objects) {
		int res = 0;

		ResultSet resultSet = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = this.getConnection();
			preparedStatement = this.getPrepareStatement(conn, "select count(*) from (" + sql + ")");

			for (int i = 0; i < objects.length; i++) {
				preparedStatement.setObject(i + 1, objects[i]); 
			}
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				res = resultSet.getInt(1); // 取出count(*) 第一个整数
				break;
			}
		} catch (Exception e) {
			except(sql, objects, e.toString());
		} finally {
			close(resultSet, preparedStatement, conn);
		}
		return res;
	}
}
