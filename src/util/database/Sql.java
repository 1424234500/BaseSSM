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

public class Sql {

	// 获取链接
	public Connection getConnection()   {
		 return null;
	}

	static void out(String s) {
		if (true) {
			System.out.println("Sql>>" + s);
		}
	}

	 
	//得到list数据
	public List<Map<String, Object>> queryList(String sql, Object... objects ){
		List<Map<String, Object>> res = null;
		ResultSet resultSet = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try{
			conn = this.getConnection();
			preparedStatement = conn.prepareStatement(sql);

			for (int i = 0; i < objects.length; i++) {
				preparedStatement.setObject(i + 1, objects[i] );	//string int obj 转换与查询问题！？？！
			}
			resultSet = preparedStatement.executeQuery();
		   
            res = rs2list(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5. 关闭数据库资源: 由里向外关闭.
			close(resultSet, preparedStatement, conn);
		}
		return res;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> rs2list(ResultSet resultSet){
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		try{
			ResultSetMetaData md = resultSet.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等   
	         int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数   
	         while (resultSet.next()) {   
	     	    Map map = new HashMap();   
		            for (int i = 1; i <= columnCount; i++) {   
		            	map.put(md.getColumnName(i), resultSet.getObject(i));   
		            }   
		            res.add(map);   
	         }   
		}catch(Exception e){
			e.printStackTrace();
		}
        return res;
	}
	
	//得到指定数据
	public Map<String, Object> queryOne(String sql, Object...objects ){
		sql = " select * from ( " + sql + " ) where rownum <= 1 ";
		List<Map<String, Object>> res = queryList(sql, objects);
		if(res != null){
			if(res.size() >= 1){
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
			preparedStatement = connection.prepareStatement(sql);
 
			for (int i = 0; i < objects.length; i++) {
				preparedStatement.setObject(i + 1, objects[i]);
			} 
			res = preparedStatement.executeUpdate();

			out("execute = " + sql + " args:" + Tools.objects2string(objects));

		} catch (Exception e) {
			out("execute error = " + sql);
			e.printStackTrace();
		} finally {
			// 5. 关闭数据库资源: 由里向外关闭.
			close(null, preparedStatement, connection);
		}
		return res;
	}

	/**
	 * 释放数据库资源的方法
	 * 
	 * @param resultSet
	 * @param preparedStatement
	 * @param connection
	 */
	public void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/** 得到当前数据库 的 当前表的 列名=id/ID 的 大于0的 当前表中并不存在的 最小的 整形的 id号 转为的 字符串 ************/
	public String getAId(String tableName, String columnName, int startI) {
		 
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int i = startI;
		boolean flag = true;
		try {
			connection = getConnection();
			//疑问：表名无法用？号占位！？
			preparedStatement = connection.prepareStatement("select  " + columnName + "  from   " + tableName ,
					ResultSet.TYPE_SCROLL_INSENSITIVE,  ResultSet.CONCUR_READ_ONLY);
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
 		} catch (SQLException e){
			e.printStackTrace();
			out("get a id error = " + e.toString());
			return "-1";
		} finally {
			// 5. 关闭数据库资源: 由里向外关闭.
			close(resultSet, preparedStatement, connection);
		}
		return "" + i;
	}

	
	//查询记录条数专用
	public int executeCount(String sql, Object... objects) {
		int res = 0;
		
		ResultSet resultSet = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try{
			conn = this.getConnection();
			preparedStatement = conn.prepareStatement(sql);

			for (int i = 0; i < objects.length; i++) {
				preparedStatement.setObject(i + 1, objects[i] );	//string int obj 转换与查询问题！？？！
			}
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {   
				res = resultSet.getInt(1);		//取出count(*) 第一个整数
				break;
			}      
		}catch(Exception e){
			Tools.out("sql.executeQuery." + e.toString());
		}finally{
			close(resultSet, preparedStatement, conn);
		}
		return res;
	}
}
