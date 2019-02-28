package util.pipe;

import util.Call;

/**
 * 管道控制器 
 *
 */
public class PipeMgr implements Call{

	private PipeMgr() {}

	public enum Type {
		PIPE,FILE,DATABASE,REDIS,REDIS_BROADCAST
	}
	public static <T> Pipe<T> getPipe(Type type, String key){
		Pipe<T> pipe = null;
		switch(type) {
		case REDIS_BROADCAST:
			pipe = (Pipe<T>) new PipeRedisBroadcastImpl();
			break;
		case REDIS:
		default:
			pipe = (Pipe<T>) new PipeRedisImpl();
			break;
		}
		pipe.start(key);
		return pipe;
	}
	
	public static Pipe<String> getPipeRedis(String key) throws PipeException {
		Pipe<String> pipe = new PipeRedisImpl();
		pipe.start(key);
		return pipe;
	}
	  
	public void call(){
	}

	


}
