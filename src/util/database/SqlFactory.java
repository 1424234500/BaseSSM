package util.database;



public class SqlFactory extends Sql {
	
	private static SqlFactory mineSql = getInstance();
	public static SqlFactory getInstance() {
		if (mineSql == null) {
			synchronized (SqlFactory.class) {
				if (mineSql == null) {
					mineSql = new SqlFactory();
				}
			}
		}
		return mineSql;
	}
	
	public SqlFactory() {
		super();
	}
}
