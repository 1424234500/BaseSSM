package util;

import org.junit.Test;

import com.walker.common.demo.ThreadSyncCreatorCoster;
import com.walker.common.util.ThreadUtil;
import com.walker.common.util.Tools;
import com.walker.common.util.ThreadUtil.Type;

public class TestCreatorCsumer {
	int newThread = 1;
	int vvv[] = new int[5];
	
	
	@Test
	public void test(){
		new ThreadSyncCreatorCoster();
		new Thread(){
			public void run(){
				while(!Thread.interrupted()){
					Tools.out("newThread", newThread++ * 5);
					ThreadUtil.sleep(5 * 1000);
				}
			}
		}.start();
		new Thread(){
			public void run(){
				while(!Thread.interrupted()){
					Tools.out("newThread", newThread * 10);
					ThreadUtil.sleep(10 * 1000);
				}
			}
		}.start();
		
		for(int j = 0; j < 40; j++){
			final int jj = j;
			ThreadUtil.execute(Type.DefaultThread, new Runnable(){
				public void run(){
					int jjj = 10;
					while(!Thread.interrupted() && jjj-- > 0 ){
						Tools.out(Type.DefaultThread, vvv[Type.DefaultThread.ordinal()]++);
						ThreadUtil.sleep(10);
					}
				}
			});
		} 
		for(int j = 0; j < 10; j++){
			final int jj = j;
			ThreadUtil.execute(Type.CachedThread, new Runnable(){
				public void run(){
					int jjj = 10;
					while(!Thread.interrupted() && jjj-- > 0 ){
						Tools.out(Type.CachedThread, vvv[Type.CachedThread.ordinal()]++);
						ThreadUtil.sleep(100);
					}
				}
			});
		} 

		for(int j = 0; j < 10; j++){
			final int jj = j;
			ThreadUtil.execute(Type.SingleThread, new Runnable(){
				public void run(){
					int jjj = 10;
					while(!Thread.interrupted() && jjj-- > 0 ){
						Tools.out(Type.SingleThread, vvv[Type.SingleThread.ordinal()]++);
						ThreadUtil.sleep(100);
					}
				}
			});
		} 
		for(int j = 0; j < 10; j++){
			final int jj = j;
			ThreadUtil.execute(Type.ScheduledThread, new Runnable(){
				public void run(){
					int jjj = 10;
					while(!Thread.interrupted() && jjj-- > 0 ){
						Tools.out(Type.ScheduledThread, vvv[Type.ScheduledThread.ordinal()]++);
						ThreadUtil.sleep(1 * 1000);
					}
				}
			});
		} 
		
		
		ThreadUtil.sleep(60 * 60 * 1000);
	}
}
