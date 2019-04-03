package com.walker.core.database;

import java.util.EnumMap;


/**
 * 连接池 管理器
 * 管理多种连接池
 *
 */
class PoolMgr{
	
	private PoolMgr() {
	}
	private static EnumMap<Type, Pool> connMap;
	static {
		connMap = new EnumMap<>(Type.class);
	}
	public static Pool getInstance() {
		return getInstance(null);
	}

	public static Pool getInstance(Type type) {
		Pool conn = connMap.get(type);
		if(conn == null){
			switch(type){
			case JDBC:
				
				break;
			case C3P0:
			default:
				conn = new PoolC3p0Impl();
			}
			connMap.put(type, conn);
		}
		return conn;
	}


}

enum Type{
	C3P0,JDBC
}



