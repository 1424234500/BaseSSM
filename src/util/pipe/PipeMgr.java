package util.pipe;

import util.Call;

/**
 * 管道控制器 
 *
 */
public class PipeMgr implements Call{

	private PipeMgr() {}
	public static Pipe<String> getPipeRedis(String key) throws PipeException {
		Pipe<String> pipe = new PipeRedisImpl();
		pipe.start(key);
		return pipe;
	}
	  
	public void call(){
	}

}

enum Type {
	PIPE,FILE,DATABASE,REDIS
}
