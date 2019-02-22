package util;

import org.junit.Test;

import util.pipe.Pipe;
import util.pipe.PipeException;
import util.pipe.PipeMgr;

public class TestPipe {

	@Test
	public void test() throws PipeException, InterruptedException {
		Pipe<String> pipe = PipeMgr.getPipeRedis("queue");
		
		
		Tools.out("size", pipe.size());
//		for(int i = 0; i < 10; i ++) {
//			Tools.out("put", pipe.put(i+""));
//		}
//		for(int i = 0; i < 8; i ++) {
//			Tools.out("get", pipe.get());
//		}

		pipe.startConsumer(4, new Fun<String>() {

			@Override
			public <T> T make(String obj) {
				try {
					long tt = (long)(Math.random() * 1000 + 1000);
					Tools.out("do something", obj, tt );
					Thread.sleep(tt);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
		});
		
		int i = 20;
		while(true) {
			long tt = (long)(Math.random() * 1000 + 1000);
			long cc = (long)(Math.random() * 10);
			i ++;
			String s = "s:" + i + "\t" + tt;
			for(int j = 0; j < cc; j++) {
				s = s + "." + j;
				pipe.put(s);
				Tools.out("make", s, tt);
			}
			Thread.sleep(tt);
		}
		
		
		
		


	}
}
