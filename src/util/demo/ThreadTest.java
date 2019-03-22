package util.demo;

import java.util.concurrent.*;

import org.apache.log4j.NDC;

import util.Tools;

/**
 * 多线程协作处理 测试
 * @author walker
 * 场景：
 * 		很多业务线程处理
 * 			每个线程分别统计
 * 		等待都处理完毕后 总线程统计
 *
 */
public class ThreadTest {
	
	/**
	 * 等待数
	 */
	CountDownLatch signal;
	/**
	 * 线程池
	 */
	ExecutorService pool;
	
	int threadPoolSize;
	int taskSize;
	
	ThreadTest(int threadPoolSize, int taskSize) {
		this.taskSize = taskSize;
		this.threadPoolSize = threadPoolSize;
		
		this.pool = Executors.newFixedThreadPool(threadPoolSize);
		this.signal = new CountDownLatch(taskSize);
	}
	
	void addTask(final String key, final Run run) {
		this.pool.execute(new Runnable() {
			@Override
			public void run() {
				NDC.push(key);
				Tools.out("begin-----------");
				run.run(key);
				Tools.out("end-----------");
				signal.countDown();
				NDC.pop();
			}
			
		});
	}
	public void await() {
		try {
			Tools.out("thread add over begin---------", this.threadPoolSize, this.taskSize);
//			this.pool.awaitTermination(99999999, TimeUnit.SECONDS);
			this.signal.await();
			Tools.out("thread add over end---------", this.threadPoolSize, this.taskSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	interface Run{
		public void run(String key);
	}
	
	
	public static void main(String[] argv) {
		ThreadTest tt = new ThreadTest(4, 20);
		for(int i =0; i < 20; i++)
			tt.addTask("t"+i, new Run() {
				@Override
				public void run(String key) {
					Tools.out(key, "down");
					try {
						Thread.sleep((long) (Math.random() * 2000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		tt.await();
		Tools.out("aaaaaaaa");
	}
	
	
}
