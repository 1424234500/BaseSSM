package util;

import java.math.BigInteger;

import org.junit.Test;

import com.walker.common.util.Fun;
import com.walker.common.util.MD5;
import com.walker.common.util.ThreadUtil;
import com.walker.common.util.Tools;

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
