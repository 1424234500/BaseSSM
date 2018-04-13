package util.socket;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import util.JsonUtil;
import util.MapListUtil;
import util.Tools;

/**
 * socket 传递 消息结构 
 *
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class Msg{
	final public static int BROADCAST = -2;		//广播所有
	final public static int BROADCAST_SYS = -1;	//广播本系统
	
	final public static int LOGIN_SERVER = 0;				//服务器登录
	final public static int LOGIN_CLIENT = 1;				//客户端登录
	
	final public static int TOSERVER = 11;			//发往服务器
	final public static int TOCLIENT = 12;			//发往客户端
	


	int msgType;		//一条消息 的类型  登录系统Msg.LOGIN 广播本系统
	String toSysKey;	//发往目标系统	也可以根据socket绑定的sysKey 和 key做 逻辑验证
	String toKey;		//发往目标客户
	
	Map map;	//消息数据包
	
	public Msg(){
		map = new HashMap();
		msgType = -999;
		toSysKey = "0";
		toKey = "0";
	}

	public Msg(String sysKey, String key, String jsonstr){
		this(jsonstr);
		this.put("fromsyskey", sysKey);
		this.put("fromkey", key);
	}
	public Msg(String jsonstr){
		map = new HashMap();
		msgType = -999;
		toSysKey = "0";
		toKey = "0";
	
        map = JsonUtil.getMap(jsonstr);
        Tools.out(map);
        msgType = Tools.parseInt(MapListUtil.getMap(map, "msgtype"));
        toSysKey = MapListUtil.getMap(map, "tosyskey");
        toKey = MapListUtil.getMap(map, "tokey");
		
		this.put("tttttttttt", "ttttttttttt");
	}
	public Msg setMsgType(int key){
		this.msgType = key;
		this.put("msgtype", key);
		return this;
	}
	
	
	
	public Msg put(Object key, Object value){
		 map.put(key, value);
		 return this;
	}
	public Object getDataMap(){
		return map;
	}
	public String getData(){
		return JsonUtil.makeJson(this.map);
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






	public String getToSysKey() {
		return toSysKey;
	}



	public void setToSysKey(String toSysKey) {
		this.put("tosyskey", toSysKey);
		this.toSysKey = toSysKey;
	}



	public String getToKey() {
		return toKey;
	}



	public void setToKey(String toKey) {
		this.put("tokey", toKey);
		this.toKey = toKey;
	}



	public int getMsgType() {
		return msgType;
	}



	@Override
	public String toString() {
		return Tools.objects2string(msgType, toSysKey, toKey, map);
	}
	 
	

}
