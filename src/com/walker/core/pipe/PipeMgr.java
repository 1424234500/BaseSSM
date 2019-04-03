package com.walker.core.pipe;

import com.walker.common.util.Call;

/**
 * 管道控制器 
 *
 */
public class PipeMgr implements Call{

	private PipeMgr() {}

	public enum Type {
		PIPE,FILE,DATABASE,REDIS,REDIS_BROADCAST
	}
	@SuppressWarnings("unchecked")
	public static <T> Pipe<T> getPipe(Type type, String key){
		Pipe<T> pipe = null;
		switch(type) {
		case REDIS_BROADCAST:
			pipe = (Pipe<T>) new PipeRedisBroadcastImpl();
			break;
		case REDIS:
			pipe = (Pipe<T>) new PipeRedisImpl();
			break;
		case PIPE:
		default:
			pipe = (Pipe<T>) new PipeListImpl<T>();
		}
		pipe.start(key);
		return pipe;
	}
	
	  
	public void call(){
	}

	


}
