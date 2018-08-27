package util.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 连接接口
 * @author walker
 *
 */
public interface Conn {
	Connection getConn() throws Exception;
	public void close(Connection conn, PreparedStatement pst, ResultSet rs) throws Exception;
	
	
}
