package util.database;

import java.util.EnumMap;

import util.cache.CacheFactory;

/**
 * 连接池 管理器
 * 管理多种连接池
 *
 */
class PoolFactory{

	private PoolFactory() {
	}
	private static EnumMap<TypePool, Pool> connMap;
	static {
		connMap = new EnumMap<>(TypePool.class);
	}
	public static Pool getInstance() {
		return getInstance(TypePool.C3P0);
	}

	public static Pool getInstance(TypePool type) {
		Pool conn = connMap.get(type);
		if(conn == null){
			switch(type){
			case C3P0:
				conn = new PoolC3p0Impl();
				break;
			case JDBC:
				
				break;
			default:
				conn = new PoolC3p0Impl();
			}
			connMap.put(type, conn);
		}
		return conn;
	}


}

enum TypePool{
	C3P0,JDBC
}



