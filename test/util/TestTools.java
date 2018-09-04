package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TestTools {

	public TestTools(){}
	
	@Test
	public void makeDay(){
		List<String> list = new ArrayList<>();
		String[] arr = {"Ⅴ", "Ⅵ", "Ⅶ", "Ⅷ"};
		String[] ttt = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
		int i = 44;
		int k = 8;
		
		for(; i < 70; i++){
			list.add("#############################################第" + i + "周 " + ttt[k%12] + "月");
			if((i+1) % 4 == 0)k++;
		}
		Collections.reverse(list);
		for(String str : list){
			System.out.println(str);
		}
	}
	
	public void testSeria(){
java.util.HashMap map = new java.util.HashMap();
util.SerializeUtil.serialize(map);
	}
	
	
	
	
	
}