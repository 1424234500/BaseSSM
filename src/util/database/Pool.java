package util.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 连接接口
 * @author walker
 *
 */
interface Pool {
	Connection getConn();
	public void close(Connection conn, PreparedStatement pst, ResultSet rs);
	
	
}
