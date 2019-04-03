package com.walker.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * org.json.tools
 */


public class JsonUtil {
	public static void main(String[] argv) {
		Bean bean = new Bean().set("key1", 23).set("key2", 232);
		Tools.out(makeJson(bean));
		List<Object> list = new ArrayList<>();
		list.add("asdf");
		list.add(bean);
		Tools.out(makeJson(list));
	}
	
	
	
	/**
	 * map转json
	 * @param obj
	 */
	public static String makeJson(Map<?,?> obj) {
        String res = "";
        try {
            JSONObject jo = new JSONObject(obj);
            res = jo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    } 
	/**
	 * list转json
	 * @param list
	 */
    public static String makeJson(Collection<?> list) {
        String res = "";
        try {
            JSONArray ja = new JSONArray(list);
            res = ja.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }  

    /**
     * 递归 解析JSONArray为list
     */
    private static List<?> toList(JSONArray ja) throws JSONException{
    	List<Object> list = new ArrayList<>();
    	for(int i = 0; i < ja.length(); i++){
        	Object object = ja.get(i);
            if (object instanceof JSONArray) {  
                JSONArray jaa = (JSONArray) object;  
                list.add(toList(jaa));
            }  
            else if (object instanceof JSONObject) {  
            	JSONObject joo = (JSONObject)object;
                list.add(toMap(joo));
            }
            else{ // 普通 类型 int string double ...
            	list.add(object);
            }
        }
    	return list;
    }
    /**
     * 递归 解析JSONObject为map
     */
    private static Bean toMap(JSONObject jo) throws JSONException{
    	Bean map = new Bean();
        //迭代多有的Key值  
        Iterator<?> it = jo.keys();  
        //遍历每个Key值  
        while (it.hasNext()) {  
            //将key值转换为字符串  
            String key = it.next().toString();  
            Object object = jo.get(key);  
            if (object instanceof JSONArray) {  
                JSONArray jaa = (JSONArray) object;  
                map.put(key, toList(jaa));
            }  
            else if (object instanceof JSONObject) {  
            	JSONObject joo = (JSONObject)object;
            	map.put(key, toMap(joo));
            }
            else{ // 普通 类型 int string double ...
            	map.put(key, object);
            }
        }  
    	return map;
    }
    /**
     * 解析json为map/list/string
     * @param jsonstr
     */
    public static <T> T get(String jsonstr) {
        try {
        	int type = getType(jsonstr);
        	if(type == 0){
        		return (T) jsonstr;
        	}
            if(type == 1){
                JSONObject jo = new JSONObject(jsonstr);
            	return (T) toMap(jo);
            }else if(type == 2){
            	JSONArray ja = new JSONArray(jsonstr);
            	return (T) toList(ja);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
	/**
	 * 0字符串 1map 2list
	 */
    public static int getType(String jsonstr){
    	int res = 0;
    	if(jsonstr != null){
	    	String str = StringUtils.strip(jsonstr);
	    	if(str.length() > 0){
	    		if(str.charAt(0) == '{' && str.charAt(str.length() - 1) == '}'){
	    			res = 1;
	    		}else if(str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']'){
	    			res = 2;
	    		}
	    	}
    	}
		return res;
    }
    public static void out(String str) {

    }
}
