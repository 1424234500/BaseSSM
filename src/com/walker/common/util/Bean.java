package com.walker.common.util;
import java.util.HashMap;
import java.util.Map;

/**
 * HashMap子类 new Bean().put("key","value").put("key2","value2);
 * @author Walker
 * 2018年7月13日
 */
public class Bean extends HashMap{ 
	private static final long serialVersionUID = 1L;


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
    public Bean(Map<?, ?> values) {
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
    public Bean put(String key, String value){
    	super.put(key, value);
    	return this;
    }
    public Bean set(String key, String value){
    	super.put(key, value);
    	return this;
    }
    public Bean set(String key, Bean value){
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
//		匹配大小写命名
//		if(obj == null)obj = get(key.toString().toLowerCase());
//		if(obj == null)obj = get(key.toString().toUpperCase());

		T res = LangUtil.turn(obj, defaultValue);
		
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
@Override
public String toString() {
	return JsonUtil.makeJson(this);
}

   

}
