package util.pipe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.nio.Buffer;


/**
 * 管道 跨 进程通信工具
 * 行字符串读取 写入
 * 同步  阻塞 阻塞队列 
 * LinkedBlockingQueue
 * ArrayBlockingQueue
 * 
 * 系统管道实现
 *
 */
class PipeImpl implements Pipe{
	PipedWriter writer;
	PipedReader reader;
	BufferedReader bufferReader;
//	BufferedWriter bufferWriter;
	
	/**
	 * 管道保活时间10分钟
	 */
	long expireTime; 
	
	PipeImpl(){
		writer = new PipedWriter();
//		bufferWriter = new BufferedWriter(writer);
		try {
			reader = new PipedReader(writer);
			bufferReader = new BufferedReader(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		expireTime = 10 * 60 * 1000;
	}
	
	@Override
	public String get(String defaultValue) {
		String res = defaultValue;
		try {
			if(reader != null){
				if(bufferReader.ready()){
					res = bufferReader.readLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public Boolean put(String str) {
		Boolean res = true;
		try {
			writer.write(str);
			writer.write('\r');
			writer.write('\n');
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			res = false;
		}
		
		return res;
	}

	@Override
	public Boolean stop() {
		try {
			bufferReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
