package util.database;

import java.util.EnumMap;

import util.cache.CacheFactory;

class PoolFactory{

	private PoolFactory() {
	}
	private static EnumMap<Type, Pool> connMap;
	static {
		connMap = new EnumMap<>(Type.class);
		CacheFactory.getInstance().put("static.ConnFactory", "connMap", connMap);
	}
	public static Pool getInstance() {
		return getInstance(Type.C3P0);
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



