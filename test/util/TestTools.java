package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.sun.org.apache.xml.internal.security.utils.XMLUtils;

import util.annotation.TrackerMgr;

public class TestTools {

	public TestTools(){}
	
	
	@Test
	public void testXml(){
		Bean test = new Bean().put("ke1", "value1").put("ke2", "value2");
		Bean bean = new Bean();
		bean.put("aaaa", "addadfadsf");
		bean.put("map", test);
		List list = new ArrayList<>();
		list.add("bbb");
		list.add("ccc");
		list.add(4);
		list.add(test);
		list.add(test);
		bean.put("list", list);
		bean.put("page", new Page());
		
		Tools.out(XmlUtil.toFullXml(bean));
		int i = 4;

		fun(i);
	}
	public void fun(Object obj){
		Tools.out(obj.getClass());
	}
	
	
	@Test
	public void testRegex(){
		//编译正则
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\[.*?\\]");
		//使用正则匹配
		java.util.regex.Matcher matcher = pattern.matcher("[aaa][bbb]ccc[ddd]");
		
		//matcher.reset(); //重置匹配位置
		while(matcher.find()){
			Tools.out(matcher.start(), matcher.end(), matcher.regionStart(), matcher.regionEnd(), matcher.group());
			
		}
			
	}	
	@Test
	public void makeDay(){
		Bean map = new Bean();
		MapListUtil.putMapUrl(map, "m", new Bean());
		MapListUtil.putMapUrl(map, "m1.m2", new Bean());
		MapListUtil.putMapUrl(map, "m1.m2.m3", "value123");
		MapListUtil.putMapUrl(map, "m2.m2.m3", "value223");
		Tools.out(map);
		
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
//			System.out.println(str);
		}
	}
	@Test
	public void testSeria(){
		java.util.HashMap map = new java.util.HashMap();
		util.SerializeUtil.serialize(map);
	}
	@Test
	public void testAnnoStart(){
//		TrackerMgr.start();
	}
	
	@Test
	public void testSplit(){
		String s = "";
		String ss[];
		ss = "".split(",");
		Tools.out(ss.length,ss);

		ss = " ".split(" ");
		Tools.out(ss.length,ss);
		ss = " ".split(" ", -1);
		Tools.out(ss.length,ss);

		ss = ",".split(",");
		Tools.out(ss.length,ss);

		ss = ",".split(",", -1);
		Tools.out(ss.length,ss);
		
	}
	
	
	
}
