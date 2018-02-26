package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * List工厂类 new MakeList<T>().add(object).add(object).build();
 * @author Walker
 * 2017年12月11日 09点42分
 */
public class MakeList { 
	List<Object> list;
	public MakeList(){
		list = new ArrayList<Object>(); 
	}
	public MakeList add(Object obj){
		list.add(obj);
		return this;
	}
	public List<Object> build(){
		return list;
	}
	 
}