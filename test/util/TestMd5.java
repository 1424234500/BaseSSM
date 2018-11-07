package util;

import java.math.BigInteger;

import org.junit.Test;

public class TestMd5 {

	@Test
	public void test(){
		
		Tools.out(MD5.encode(new BigInteger("234"), 6, '0', 'z'));
		MD5.parse("aaaa", 4, 4, new Fun<String>(){
			@Override
			public String make(String obj) {
				return obj; 
			}
			
		});
		
		ThreadUtil.sleep(1000 * 60 * 60 * 2222);

	}
	
	
}
