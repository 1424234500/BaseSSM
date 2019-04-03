package com.walker.socket.server_0;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.walker.common.util.JsonUtil;
import com.walker.common.util.MapListUtil;
import com.walker.common.util.Tools;

/**
 * socket 传递 消息结构 
 *
 */
@SuppressWarnings({"rawtypes", "unchecked" })
public class Msg{
	final public static int SHOW = -1000;				//监控

	final public static int BROADCAST = -2;		//广播所有
	final public static int BROADCAST_SYS = -1;	//广播本系统
	
	final public static int LOGIN = 0;				//服务器/客户端登录
	final public static int RES = 1;				//发送结果提示用

	final public static int TOSERVER = 11;			//发往服务器
	final public static int TOCLIENT = 12;			//发往客户端
	final public static int DATA = 10;				//文本消息 请求转发
	

	//决定转发规则
	int msgType;		//一条消息 的类型  登录系统Msg.LOGIN 广播本系统 
	String toSysKey;	//发往目标系统	也可以根据socket绑定的sysKey 和 key做 逻辑验证
	String toKey;		//发往目标客户
	String fromSysKey;	//来自系统
	String fromKey;		//来自服务器

	//消息id 状态
	String id;		//消息id
	String ok;		//传输结果
	String info;	//说明
	//消息实体
	Map data;		//消息数据包
	
	public Msg(){
		id = UUID.randomUUID().toString();
		ok = "";
		info = "";
		data = new HashMap();
		
		msgType = -2;
		toSysKey = "";
		toKey = "";
		fromSysKey = "";
		fromKey = "";
	}
	public Msg(boolean ifNull){
		if(ifNull){
			id = UUID.randomUUID().toString();
		}
	}
	public Msg(String jsonstr){
        Map map = JsonUtil.get(jsonstr);
        this.setOk(MapListUtil.getMap(map, "ok", "0"));
        this.setInfo(MapListUtil.getMap(map, "in", ""));
        this.setId(MapListUtil.getMap(map, "id", ""));
        this.setData((Map) MapListUtil.getMap(map, "data", new HashMap()));

        this.setMsgType(Tools.parseInt(MapListUtil.getMap(map, "mt", "-2")));
        this.setToSysKey(MapListUtil.getMap(map, "tsk", ""));
        this.setToKey(MapListUtil.getMap(map, "tk", ""));
        this.setFromSysKey(MapListUtil.getMap(map, "fsk", ""));
        this.setFromKey(MapListUtil.getMap(map, "fk", ""));
	}
	
	//回传消息
	public Msg getEcho(boolean ok, String info){
		Msg msg = new Msg(false);
		msg.setMsgType(Msg.RES);
		msg.setId(this.id);
		msg.setOk(ok);
		msg.setInfo(info);
		return msg;
	}
	
	
	
	
	public String getData(){
		Map m = new HashMap();
		if(Tools.notNull(ok)) m.put("ok", ok);
		if(Tools.notNull(id)) m.put("id", id);
		if(Tools.notNull(info)) m.put("in", info);
		if(data != null && data.size() > 0) m.put("data", data);

		if(Tools.notNull(msgType)) m.put("mt", msgType);
		if(Tools.notNull(toSysKey)) m.put("tsk", toSysKey);
		if(Tools.notNull(toKey)) m.put("tk", toKey);
		if(Tools.notNull(fromSysKey)) m.put("fsk", fromSysKey);
		if(Tools.notNull(fromKey)) m.put("fk", fromKey); 
		
		return JsonUtil.makeJson(m);
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public void setInfo(String info) {
		this.info = info;
	}

	public String getOk() {
		return ok;
	}
	public void setOk(String ok){
		this.ok = ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok?"1":"0";
	}

	public Msg setMsgType(int key){
		this.msgType = key;
		return this;
	}
	
	
	
	public Msg put(Object key, Object value){
		 data.put(key, value);
		 return this;
	}
	public Object getDataMap(){
		return data;
	}
	
	public Object get(Object key){
		return this.get(key, null);
	}

    public <T> T get(Object key, T defaultValue){
        Object res = data.get(key);
        if(res == null)
            res = defaultValue;
        return (T)res;
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
		this.data = data;
		return this;
	}
	public String getDataJson(){
        return JsonUtil.makeJson(this.data);
    }

	@Override
	public String toString() {
		return this.getData();
	}
	 
	

}
