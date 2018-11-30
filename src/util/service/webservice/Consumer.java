package util.service.webservice;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import util.Tools;
import util.service.webservice.client.ServiceClass.ServiceClassImpl;
import util.service.webservice.client.ServiceClass.ServiceClassImplService;


/**
 * Consumer
 * 模拟访问客户端
 * 
 */
public class Consumer {

	
	
	public static Object doWsdl(String url, Object...objects){
		
		return null;
	}
	
	
	@Test
	public void test(){
		ServiceClassImplService webService = new ServiceClassImplService();  
	    ServiceClassImpl service = webService.getServiceClassImplPort();
	    Tools.out(service.test("hello world !"));
	    
	    List<Object> list = new ArrayList<>();
//	    Map map = new HashMap<>();
//	    map.put("id", "idddd");
//	    map.put("key", "kkkkk");
//	    list.add(map);
//	    list.add("{\"id\":\"000\", \"key\":\"222\" } ");
	    list.add("asdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdfasdkfjaskldfjalsdfjaksldfjaslkdf");
//	    list.add("");
//	    Page page = new Page();
//	    list.add(page);
	    //反射 page 对象无法传递 spring autoWire自动实例化 无法实例化 先后顺序待定
	    Object obj = service.doClassMethod("util.Tools","tooLongCut", list);
		System.out.println(obj);
	    System.out.println("________");
	    
//		public List<Map<String, Object>> list(String id, String name, String timefrom, String timeto, Page page) {

	}
	
	
	
	
}