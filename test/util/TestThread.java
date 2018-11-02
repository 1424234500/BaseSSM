package util;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

import util.ThreadUtil.Type;

public class TestThread {

	static int iSignal = 0;

	/**
	 * 测试线程相关
	 */
	@Test
	public void ttt( ){
		final Lock lock = new ReentrantLock();
		final Condition condition = lock.newCondition(); //依赖于锁lock的信号标识量  需要持有锁的状态才能用 否则异常
		Tools.out("---------------------------tttttttt-------------");

		ThreadUtil.execute(new Runnable() {
			@Override
			public void run() {
				while(! Thread.interrupted()){
					lock.lock();
					try{
						Tools.out("execute 0:" + iSignal);
						iSignal++;
//						Thread.yield();
						ThreadUtil.sleep(3000);
						condition.notifyAll();
					}finally{
						lock.unlock();
					}
				}
			}
		});
		ThreadUtil.execute(new Runnable() {
			@Override
			public void run() {
				while(! Thread.interrupted()){
					if(iSignal % 3 == 1){
						lock.lock();
						try{
							Tools.out("execute 1:" + iSignal);
							iSignal++;
							ThreadUtil.sleep(1000);
							condition.notifyAll();
						}finally{
							lock.unlock();
						}
					}else{
						try {
							condition.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		Future<?> f = ThreadUtil.submit(Type.DefaultThread, new Runnable() {
			@Override
			public void run() {
				while(! Thread.interrupted()){
					if(iSignal % 3 == 2){
						lock.lock();
						try{
							Tools.out("execute 2:" + iSignal);
							iSignal++;
							ThreadUtil.sleep(1000);
							condition.notifyAll();
						}finally{
							lock.unlock();
						}
					}else{
						try {
							condition.await(); //等待condition条件信号
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
//		f.cancel(true);
		
//		try {
//			wait(); //非同步控制方法里调用  0.可以由notify恢复 1.释放锁  所以异常 调用时必须要获得锁才能?    ThreadUtil.sleep不释放锁  不可恢复 只是暂时让出cpu
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		Object lockObj = new Object();
		synchronized (lockObj) {
			lockObj.notifyAll();//唤醒所有持有该锁?
		}
		
		
		ThreadUtil.awaitTermination(Type.DefaultThread, 60 * 1000 * 30, TimeUnit.MILLISECONDS);
		
		
		
	}
}
