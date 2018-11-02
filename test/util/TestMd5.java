package util;

import java.math.BigInteger;

import org.junit.Test;

public class TestMd5 {

	@Test
	public void test(){
		
		Tools.out(MD5.encode(new BigInteger("1829312831729837128937128937189237"), 6, '0', 'z'));
		MD5.parse("aaaa", 4, 4, new Fun<String>(){
			@Override
			public String make(String obj) {
				return obj;
			}
			
		});


	}
	
	
}
