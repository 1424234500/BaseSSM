package util.pipe;

import util.Call;

/**
 * 管道控制器 
 *
 */
public class PipeMgr implements Call{

	private static Pipe pipe = null;

	public PipeMgr() {
	}
	public static Pipe getInstance() {
		if (pipe == null) {
			pipe = getInstance(Type.PIPE);
			reload(pipe);
		}
		return pipe;
	}

	public static Pipe getInstance(Type type) {
		Pipe pipe = null;
		switch (type) {
		case PIPE:
			pipe = new PipeImpl();
			break;
		case FILE:
			break;
		case DATABASE:
			break;
		default:
			pipe = new PipeImpl();
		}
		return pipe;
	}

	/**
	 * 初始化pipe 系统级数据 环境设置读取 词典加载 额外配置项
	 * 1.加载配置文件
	 * 2.加载数据库 
	 */
	public static void reload(Pipe pipe){
		pipe.put("Start");
	}
	
	public void call(){
		Pipe pipe = getInstance();
		pipe.put("Call");
	}

}

enum Type {
	PIPE,FILE,DATABASE,
}
