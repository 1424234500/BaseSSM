package util;

import org.junit.Test;

import util.database.Dao;

public class TestDatabase {
	public void test0(int size){
		long time = System.currentTimeMillis();
		Dao dao = new Dao();
		int  bcount = dao.count("select * from student");

		for(int i = 0; i < size; i++){
			String name = "test" + i;
			dao = new Dao();
			dao.executeSql("insert into student(id,name) values(seq_student.nextval,?)", name);
		}
		dao = new Dao();
		int  acount = dao.count("select * from student");
		Tools.out(size, bcount, acount, Tools.calcTime(System.currentTimeMillis() - time));
	}
	public void test1(int size){
		long time = System.currentTimeMillis();

		Dao dao = new Dao();
		int  bcount = dao.count("select * from student");
		for(int i = 0; i < size; i++){
			String name = "test" + i;
			dao.executeSql("insert into student(id,name) values(seq_student.nextval,?)", name);
		}
		int  acount = dao.count("select * from student");
		Tools.out(size, bcount, acount, Tools.calcTime(System.currentTimeMillis() - time));

	}

	public void test2(int size){
		long time = System.currentTimeMillis();

		Dao dao = new Dao();
		
		int  bcount = dao.count("select * from student");

		for(int i = 0; i < size; i++){
			String name = "test" + i;
			dao.executeSql("insert into student(id,name) values(seq_student.nextval,?)", name);
		}
		int  acount = dao.count("select * from student");
		Tools.out(Thread.currentThread(), size, bcount, acount, Tools.calcTime(System.currentTimeMillis() - time));
	}
	public void test3(int size){
		long time = System.currentTimeMillis();


		for(int i = 0; i < size; i++){
			Bean bean = new Bean().put("key", "value").put("key1", "value").put("key2", "value");

//			bean.get("key");
		}
		Tools.out(size, Tools.calcTime(System.currentTimeMillis() - time));
	}
	@Test
	public void test(){
		int[] ss = {100, 1000, 5000};
		for(int size : ss){
//			test0(size);
//			test1(size);
//			test2(size);
//			test3(size);
		}
		
		
		for(int i = 0; i < 1; i++){
			ThreadUtil.execute(
				new Runnable(){
					public void run(){
						test2(2000);
					}
				}
			);
			ThreadUtil.sleep(10);

		}
		
		ThreadUtil.sleep(3600 * 1000);
	}
	
	
}
