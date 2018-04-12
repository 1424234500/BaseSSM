package util.socket;

import java.util.HashMap;
import java.util.Map;

/**
 * socket 传递 消息结构 
 *
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class Msg{
	final static int BROADCAST = -2;		//广播所有
	final static int BROADCAST_SYS = -1;	//广播本系统
	
	final static int LOGIN_SERVER = 0;				//服务器登录
	final static int LOGIN_CLIENT = 1;				//客户端登录
	
	final static int TOSERVER = 11;			//发往服务器
	final static int TOCLIENT = 12;			//发往客户端
	


	int key;	//一条消息 的类型  登录系统Msg.LOGIN
	Map map;	//消息数据包
	
	public Msg setKey(int key){
		this.key = key;
		return this;
	}
	public Msg put(Object key, Object value){
		 map.put(key, value);
		 return this;
	}
	
	public Object getData(){
		return map;
	}
	public Object get(Object key){
		return this.get(key, null);
	}
	public Object get(Object key, Object defaultValue){
		 Object res = map.get(key);
		 if(res == null)
			 res = defaultValue;
		 return res;
	}
	
	
	

}
