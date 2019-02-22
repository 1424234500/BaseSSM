package util.database;

import java.util.EnumMap;

import util.cache.CacheMgr;

/**
 * 连接池 管理器
 * 管理多种连接池
 *
 */
class PoolMgr{
	
	private PoolMgr() {
	}
	private static final Type DEFAULT_TYPE=Type.C3P0;
	private static EnumMap<Type, Pool> connMap;
	static {
		connMap = new EnumMap<>(Type.class);
	}
	public static Pool getInstance() {
		return getInstance(DEFAULT_TYPE);
	}

	public static Pool getInstance(Type type) {
		Pool conn = connMap.get(type);
		if(conn == null){
			switch(type){
			case C3P0:
				conn = new PoolC3p0Impl();
				break;
			case JDBC:
				
				break;
			default:
				return getInstance(DEFAULT_TYPE);
			}
			connMap.put(type, conn);
		}
		return conn;
	}


}

enum Type{
	C3P0,JDBC
}



