package util;

import util.route.*;
import util.route.SubPub.OnSubscribe;

/**
 * 订阅发布模型
 *
 */
public class TestPub {
 
	public  static void main(String[] argv) {
		final SubPub<String> sp = SubPubMgr.getSubPub(4);
		final String channel = "c";
		sp.subscribe(channel+"0", new OnSubscribe<String>() {
			@Override
			public Type onSubscribe(String object) {
				Tools.out("subscribe", object);
				return Type.DEFAULT;
			}
		});
		for(int i = 0; i < 6; i++) {
			final int ii = i;
			new Thread() {
				public void run() {
					for(int j = 0; j < 10; j++){
						String k = "o:" + ii + ":" + j;
						String c = channel + (int)(Math.random() * 0);
						Tools.out("channel", c, "publish", k);
						sp.publish(c, k);
					}
				}
			}.start();
		}
		
		
		
		
		
		
		
		while(true) {
//			ThreadUtil.sleep(10);
		}
	}
	
	
	
	
}




