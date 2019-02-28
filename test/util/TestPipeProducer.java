package util;

import org.junit.Test;

import util.pipe.Pipe;
import util.pipe.PipeException;
import util.pipe.PipeMgr;

public class TestPipeProducer {
	@Test
	public void testProducer() {
		for ( int i = 0; i < 1; i++) {
			new Thread() {
				public void run() {
					Pipe<String> pipe = PipeMgr.getPipe(PipeMgr.Type.REDIS_BROADCAST, "broadcast");
					int now = 0;
					while(now < 10000) {
						int tt = (int) (Math.random() * 10);
						int cc = (int) (Math.random() * 8);
						now++;
						String str = "make." + now + "." + Tools.getNowTimeS();
						for(int i = 0; i < cc; i++) {
							str += i + ".";
							pipe.put(str);
						}
						try {
							Thread.sleep(tt);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
		while(true) {
			
		}
	}


}
