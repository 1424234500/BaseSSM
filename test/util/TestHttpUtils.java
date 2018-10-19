package util;

import org.junit.Test;

public class TestHttpUtils {

	@Test
	public void test() throws Exception {
		String str = HttpUtils.get("https://blog.csdn.net/she_smiled/article/details/79336948");
		Tools.out(str);
		
		String str1 = HttpUtils.post("http://localhost:8088/BaseSSM/angular/listrecent.do",
				new Bean().put("ID", "1").put("NAME", "aaa"));
		Tools.out(str1);
		
	}
}
