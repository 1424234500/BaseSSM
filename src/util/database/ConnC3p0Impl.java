package util.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 连接接口 c3p0实现
 * 
 * @author walker
 *
 */
public class ConnC3p0Impl implements Conn {
	static Logger logger = org.apache.log4j.Logger
			.getLogger(ConnC3p0Impl.class);

	// 通过标识名来创建相应连接池
	static ComboPooledDataSource dataSource = new ComboPooledDataSource(
			"oracle");

	@Override
	public Connection getConn() throws Exception {
		try {
			return dataSource.getConnection();
		} catch (Exception e) {
			logger.error("Exception in C3p0Utils!", e);
			throw new Exception("数据库连接出错!", e);
		}
	}

	@Override
	public void close(Connection conn, PreparedStatement pst, ResultSet rs) throws Exception {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Exception in C3p0Utils! ResultSet", e);
				throw new Exception("数据库连接关闭出错!", e);
			}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				logger.error("Exception in C3p0Utils! PreparedStatement", e);
				throw new Exception("数据库连接关闭出错!", e);
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Exception in C3p0Utils! Connection", e);
				throw new Exception("数据库连接关闭出错!", e);
			}
		}
	}

}
