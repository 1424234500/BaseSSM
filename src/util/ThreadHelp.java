package util;

/**
 * 线程工具
 * @author Walker
 *
 */
public class ThreadHelp {
	/**
	 * 线程工具 耗时处理
	 * @param fun
	 * @return 回调线程id
	 */
	public static  Thread thread(final Fun<Long> fun){
		Thread thread = new Thread(){
			public void run(){
				if(fun != null){
					fun.make(this.getId());;
				}
			}
		} ; 
		return thread;
	}
	public static void sleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			//e.printStackTrace();
			System.out.println("Thread.sleep error " + time);
		}
	}
}