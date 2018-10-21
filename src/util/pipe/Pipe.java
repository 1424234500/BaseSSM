package util.pipe;


/**
 * 管道 跨 进程通信工具
 * 行字符串读取 写入
 * 同步  阻塞 阻塞队列 
 * LinkedBlockingQueue
 * ArrayBlockingQueue
 * 
 * 系统管道实现
 * 文件实现
 * 数据库实现
 *
 */
public interface Pipe{
	
	/**
	 * 获取最先写入的数据
	 */
	String get(String defaultValue);
	/**
	 * 写入数据到尾巴
	 * @return 是否写入成功
	 */
	Boolean put(String str);

	/**
	 * 关闭
	 */
	Boolean stop();
	
	
	
	
	
	
	
	
	
	
	
	
	
}
