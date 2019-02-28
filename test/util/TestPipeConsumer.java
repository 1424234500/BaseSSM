package util;

import org.junit.Test;

import util.pipe.Pipe;
import util.pipe.PipeException;
import util.pipe.PipeMgr;

public class TestPipeConsumer {
	
	@Test
	public void test() throws PipeException, InterruptedException {
//		Pipe<String> pipe = PipeMgr.getPipe(PipeMgr.Type.REDIS, "queue");

		Pipe<String> pipe = PipeMgr.getPipe(PipeMgr.Type.REDIS_BROADCAST, "broadcast");
		
		
		Tools.out("size", pipe.size());

		pipe.startConsumer(4, new Fun<String>() {

			@Override
			public <T> T make(String obj) {
				try {
					long tt = (long)(Math.random() * 60);
					Tools.out("do something", obj, tt );
					Thread.sleep(tt);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
			
		});
		
		
		
		
		while(true) {
			
		}


	}
}
