package util;

import java.util.*;

import util.pipe.Pipe;
import util.pipe.PipeException;
import util.pipe.PipeMgr;
import util.pipe.PipeMgr.Type;

public class TestMain {
	TestMain() throws PipeException, InterruptedException{
		
		Map<String, Object> map = new HashMap<>();
		
		for(int i = 0; i < 69; i++) {
			map.put("k"+i, i);
		}
		Tools.out(map.size());
		
		Pipe<String> pipe = PipeMgr.getPipe(Type.REDIS, "queue");

		int i = 20;
		while(true) {
			long tt = (long)(Math.random() * 1000 + 1000);
			long cc = (long)(Math.random() * 10);
			i ++;
			String s = "s:" + i + "\t" + tt;
			for(int j = 0; j < cc; j++) {
				s = s + "." + j;
				pipe.put(s);
//				Tools.out("make", s, tt);
			}
			Thread.sleep(tt);
		}
		
	}
	
	
	public static void main(String[] args) throws Exception {
		new TestMain();
	}

}
