package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * HashMap子类 new Bean().put("key","value").put("key2","value2);
 * @author Walker
 * 2018年7月13日
 */
public class Bean extends HashMap{ 
	public static Bean getBean(){
		return new Bean();
	}
    /**
     * 构造体方法
     */
    public Bean() {
        super();
    }
    
    /**
     * 构造体方法，直接初始化Bean
     * @param values    带数据信息
     */
    public Bean(HashMap<Object, Object> values) {
        super(values);
    }

    /**
     * 设置对象，支持级联设置
     * @param key   键值
     * @param obj   对象数据
     * @return this，当前Bean
     */
    public Bean set(Object key, Object value) {
        put(key, value);
        return this;
    }
    public Bean put(Object key, Object value){
    	super.put(key, value);
    	return this;
    }

    /**
     * 获取对象值，如果不存在则返回缺省对象
     * 
     * @param key 键值
     * @param def 缺省对象
     * @return 对象值
     */ 
	public <T> T get(Object key, T defaultValue){
		Object obj = get(key);
		T res = null;
		if(obj == null)obj = get(key.toString().toLowerCase());
		if(obj == null)obj = get(key.toString().toUpperCase());

		if(obj == null) {
			res = defaultValue;
		}else{
			if (defaultValue instanceof String) {
				res = (T)(obj.toString());
			} else if (obj instanceof String) {
				if (defaultValue instanceof Integer) {
					res = (T)(new Integer(Tools.parseInt(obj.toString())));
				} else if (defaultValue instanceof Double) {
					res = (T)(new Double(Tools.parseDouble(obj.toString())));
				}else{
					res = (T)obj;
				}
			}else{
				res = (T)obj;
			}
		}

		return res;
	}
    

    /**
     * 删除指定值
     * 
     * @param key 键值
     * @return 当前对象
     */
    public Bean remove(Object key) {
        if (containsKey(key)) {
            super.remove(key);
        }
        return this;
    }

 
    /**
    * 将在数组中设定属性键值的内容传递到另外一个bean
    * @param keys  键值数组 null表示传全部src中的数据
    * @return 复制出来的数据内容，
    */
   public Bean copyOf(Object... keys)  {
       Bean tar = new Bean(); 
       if (keys != null) {
           for (Object key : keys) {
               tar.set(key, get(key));
           }
       } else {
           tar.putAll(this);
       }
       return tar;
   }
   
   /**
    * @param bean 将指定对象的数据复制到本对象中
    */
   public void copyFrom(Bean bean) {
       copyFrom(bean, null);
   }
   
   /**
    * @param bean 将指定对象的数据复制到本对象中
    * @param keys 指定键值
    */
   public void copyFrom(Bean bean, Object ...keys) {
       if (keys != null) {
           for (Object key : keys) {
               if(bean.containsKey(key)) {
                   set(key, bean.get(key));
               }
           }
       } else {
           putAll(bean);
       }
   }


   public static void main(String[] argc){
	   Bean bean = Bean.getBean().put("key1", "v1").put("key2", "v2");
	   List<String> list = new ArrayList<>();
	   list.add("aaa");
	   list.add("bbb");
	   Bean bean1 = Bean.getBean().put("key1", "v1").put("key2", "v2")
			   .put("bean", bean)
			   .put("list", list)
			   ;
	   
	   Tools.out(bean);
	   Tools.out(bean.get("key1",""));
	   Tools.out(bean.get("key3",new HashMap()));
	   Tools.out(bean1);
	   Tools.out(bean1.get("bean",new HashMap()));
	   Tools.out(bean1.get("list"));
	   Tools.out(bean1.get("list2"));
	   
	   
   }


}
