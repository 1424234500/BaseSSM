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
	final public static int SHOW = -1000;				//监控

	final public static int BROADCAST = -2;		//广播所有
	final public static int BROADCAST_SYS = -1;	//广播本系统
	
	final public static int LOGIN = 0;				//服务器/客户端登录
	
	final public static int TOSERVER = 11;			//发往服务器
	final public static int TOCLIENT = 12;			//发往客户端
	final public static int DATA = 10;				//文本消息 请求转发
	


	int msgType;		//一条消息 的类型  登录系统Msg.LOGIN 广播本系统
	String toSysKey;	//发往目标系统	也可以根据socket绑定的sysKey 和 key做 逻辑验证
	String toKey;		//发往目标客户
	String fromSysKey;	//来自系统
	String fromKey;		//来自服务器

	String info;	//说明
	String ok;		//传输结果
	Map map;			//消息数据包
	
	public Msg(){
		map = new HashMap();
		msgType = -999;
		toSysKey = "0";
		toKey = "0";
		fromSysKey = "0";
		fromKey = "0";
	}
 
	public Msg(String jsonstr){
        Map map = JsonUtil.getMap(jsonstr);
//        Tools.out(tmap);
        this.setOk(MapListUtil.getMap(map, "ok", ""));
        this.setInfo(MapListUtil.getMap(map, "info", ""));
        this.setMsgType(Tools.parseInt(MapListUtil.getMap(map, "msgtype", "0")));
        this.setToSysKey(MapListUtil.getMap(map, "tosyskey", "0"));
        this.setToKey(MapListUtil.getMap(map, "tokey", "0"));
        this.setFromSysKey(MapListUtil.getMap(map, "fromsyskey", "0"));
        this.setFromKey(MapListUtil.getMap(map, "fromkey", "0"));
        this.setData((Map) MapListUtil.getMap(map, "data", new HashMap()));
	}
	public String getData(){
		Map m = new HashMap();
		m.put("ok", this.getOk());
		m.put("info", this.getInfo());
		m.put("msgtype", this.getMsgType());
		m.put("tosyskey", this.getToSysKey());
		m.put("tokey", this.getToKey());
		m.put("fromsyskey", this.getFromSysKey());
		m.put("fromkey", this.getFromKey()); 
		m.put("data", this.getDataMap());
		return JsonUtil.makeJson(m);
	}
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getOk() {
		return ok;
	}

	public void setOk(String ok) {
		this.ok = ok;
	}

	public Msg setMsgType(int key){
		this.msgType = key;
		return this;
	}
	
	
	
	public Msg put(Object key, Object value){
		 map.put(key, value);
		 return this;
	}
	public Object getDataMap(){
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




	public String getFromSysKey() {
		return fromSysKey;
	}

	public void setFromSysKey(String fromSysKey) {
		this.fromSysKey = fromSysKey;
	}

	public String getFromKey() {
		return fromKey;
	}

	public void setFromKey(String fromKey) {
		this.fromKey = fromKey;
	}

	public String getToSysKey() {
		return toSysKey;
	}

	public void setToSysKey(String toSysKey) {
		this.toSysKey = toSysKey;
	}

	public String getToKey() {
		return toKey;
	}



	public void setToKey(String toKey) {
		this.toKey = toKey;
	}



	public int getMsgType() {
		return msgType;
	}

	public Msg setData(Map data){
		this.map = data;
		return this;
	}


	@Override
	public String toString() {
		return this.getData();
	}
	 
	

}
