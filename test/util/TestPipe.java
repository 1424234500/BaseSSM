package util;

import org.junit.Test;

import util.pipe.Pipe;
import util.pipe.PipeMgr;

public class TestPipe {

	@Test
	public void test() {
		Pipe pipe = PipeMgr.getInstance();
		Tools.out(pipe.put("aaaaaaa"));
		Tools.out(pipe.put("ccc"));
		Tools.out(pipe.put("ddddddd"));
		
		Tools.out(pipe.get(null));
		Tools.out(pipe.get(null));
		Tools.out(pipe.get(null));
		Tools.out(pipe.get(null));
		Tools.out(pipe.get(null));
		Tools.out(pipe.get(null));
		Tools.out(pipe.get(null));
		

	}
}
