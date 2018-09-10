package util;

import java.sql.Connection;

import org.junit.Test;

import util.database.Conn;
import util.database.ConnFactory;

public class TestDatabase {

	@Test
	public void test(){
		Conn conn = ConnFactory.getInstance();
		Connection connection = conn.getConn();
		conn.close(null, null, null);
		
		
		
		
		
	}
	
	
	
}
