package com.walker.socket.server_0;

import java.util.HashMap;

import com.walker.common.util.Tools;

/**
 * 一种逻辑管理的服务 实现  抽离转发规则
 * 由底层控制调用 此 又回归底层调用实现
 * 使用HashMap<String, HashMap<String, SOCKET>>管理所有连接和定义转发规则
 * 再由实际的底层调用此规则
 */
public  class ServerHashmapImpl<SOCK> implements Server<SOCK>{
	

	/**
	 * 底层实现 由实现类setThis 使this.do 调用  底层 frame.do
	 */
	SocketFrame<SOCK> frame = null;	//此怼底层引用
	public ServerHashmapImpl(SocketFrame<SOCK> frame){
		this.frame = frame;			//此对底层引用
		this.frame.setServer(this);	//底层对此引用
	}
	
	/**
	 * 上层调用 底层 实现 发送
	 * 向某个客户发送消息
	 */
	@Override
	public void send(SOCK obj, String jsonstr){
		out("发出", jsonstr);
		this.frame.send(obj, jsonstr);
	}
	/**
	 * 处理业务转发逻辑 底层调用这里 事件触发
	 */
	@Override
	public void onReceive(SOCK obj, String jsonstr){
		this.doMsg(obj, jsonstr);
	}
	/**
	 * 1.java.net.Socket
	 * 底层调用 事件触发
	 */
	@Override
	public void onNewConnection(SOCK obj) {
		//新连接 添加为自己掌管的客户 socket
		ToClient<SOCK> toClient = new ToClient<SOCK>(obj);
		this.addClient(DEFAULT_SYSKEY, toClient);
		out("新连接", toClient);
	} 
	/**
	 * 断开连接 事件触发
	 */
	@Override
	public void onDisConnection(SOCK obj) {
		//新连接 添加为自己掌管的客户 socket
		ToClient<SOCK> toClient = this.getClient(obj);
		out("断开连接", toClient);
		if(toClient != null)
			this.removeClient(toClient.getSysKey(), toClient.getKey());
	}
	@Override
	public boolean start() {
		this.frame.start();
		return true;
	} 
	@Override
	public boolean stop() {
		this.frame.stopImpl();
		this.frame = null;
		return true;
	} 
	public void send(SOCK sock, Msg msg){
		send(sock, msg.getData());
	}
	public void send(ToClient<SOCK> client, Msg msg){
		send(client.getSocket(), msg.getData());
	}
	public void sendAllSys(Msg msg){
		msg.setInfo("广播 全系统");
		for(String sysKey : toClients.keySet()){
			for(String key : toClients.get(sysKey).keySet()){
				send(toClients.get(sysKey).get(key).getSocket(), msg);
			}
		}
	}
	public void sendSys(String sysKey, Msg msg){
		msg.setInfo("广播 本系统");
		for(String key : toClients.get(sysKey).keySet()){
			send(toClients.get(sysKey).get(key).getSocket(), msg);
		}
	}
	
	
	
	
	
	
	
	
	//路由表？？？
		//索引! 系统key       系统子级key  客户端引用
	HashMap<String, HashMap<String, ToClient<SOCK>>> toClients = new HashMap<String, HashMap<String, ToClient<SOCK>>>();
	final static String DEFAULT_SYSKEY = "0";
	final static String DEFAULT_KEY = "0";
	
//	<0, <0, obj>> this default
//	<0, <1001, obj>> this 未登录1
//	<0, <1002, obj>> this 未登录2

//	<10a0, <0,    obj>> 10a0服务器-this 
//	<10a0, <1001, obj>> 10a0服务器-1 
	
	
	 
	/**
	 * 从底层传递上来的 收到的消息 
	 * 此处负责找到该消息 对应的当前管理的 那个ToClient 并调用处理逻辑
	 * Arg -> socket -> ToClient -> sysKey,key
	 * str -> Msg(msgType, toSysKey, toKey, map)
	 */
	public void doMsg(SOCK sock, String jsonstr){
		out("收到", jsonstr);

		ToClient<SOCK> toClient = null;
		ToClient<SOCK> fromClient = this.getClient(sock);	//发送者<fromSys,from,socket>
		if(fromClient == null) return;

		Msg msg = new Msg(jsonstr);	//消息<fromSys,from,toSys,to>

		String fromSysKey = fromClient.getSysKey();	//发送者   被记录认可的身份from
		String fromKey = fromClient.getKey();
		String toSysKey = msg.getToSysKey();		//目的地址
		String toKey = msg.getToKey();
		int msgType = msg.getMsgType();
		msg.setFromKey(fromKey);		//设置消息记录发送者路由
		msg.setFromSysKey(fromSysKey);	//接收端会收到 来自那里kk 并发送自目标kk  a - ss - s -k ss -k a -k ss - akb
		
		msg.setToKey("");				//清空目的地 接收者不需要消息的目的地（自己）
		msg.setToSysKey("");
		
		if(msgType == Msg.SHOW){	//服务器/客户端登录 未登录某系统时 都归属于 this 0/1000+
			msg.setInfo("获取所有在线用户列表");
			msg.put("res", show());
			msg.setOk(true);
			send(sock, msg); 
		}else if(msg.getMsgType() == Msg.BROADCAST){	//广播所有系统 不论认证 测试用
			send(sock, msg.getEcho(true, "广播"));	//成功中转
			sendAllSys(msg);
		}else if(msg.getMsgType() == Msg.LOGIN){	//服务器/客户端登录 未登录某系统时 都归属于 this 0/1000+
			if(toKey.length() > 3){
				this.changeServer(fromSysKey, fromKey, toSysKey);
			}else{
				this.changeClient(fromSysKey, fromKey, toSysKey);
			}
			show();
			msg.setOk(true);
			msg.setInfo("认证中转站成功");
			fromClient = this.getClient(sock);
			msg.setFromSysKey(fromClient.getSysKey());
			msg.setFromKey(fromClient.getKey());
			send(sock, msg);
		}else if( ! fromSysKey.equals(DEFAULT_SYSKEY)){	//不属于默认管制 已经认证 
			out("解析结构", msg.getData());
			if(msg.getMsgType() == Msg.BROADCAST_SYS){
				send(sock, msg.getEcho(true, "中转成功"));
				sendSys(toSysKey, msg);
			}else if(msg.getMsgType() == Msg.DATA){		//中转消息
				toClient = this.getClient(toSysKey, toKey);
				if(toClient == null){
					send(sock, msg.getEcho(false, "不在线"));
				}else{
					send(toClient, msg);	//发往目标
					send(sock, msg.getEcho(true, "中转成功"));
				}
			}
			
			
			
		}else{
			msg.setOk("false");
			msg.setInfo("Please login in, (msgType=0,toSysKey=sys001,toKey=pwd) ");
			send(sock, msg);
		}
		
		

		
 
		
	}
	


 

	
	
	
	
	
	
	//获取某系统下一个最小的不重复的编码
	private String getNewKey(String sysKey){
		HashMap<String, ToClient<SOCK>> sys = getSys(sysKey);
		String res = "";
		int start = 1000;
		for(String key : sys.keySet()){
			if(!key.equals(start++ +""))
				break;
		}
		res = start + "";	
		return res;
	}
	
	

	private ToClient<SOCK> changeServer(String sysKey, String key, String toSysKey){
		return this.addServer(toSysKey, this.removeClient(sysKey, key));
	}
	private ToClient<SOCK> changeClient(String sysKey, String key, String toSysKey){
		return this.addClient(toSysKey, this.removeClient(sysKey, key));
	}
	private ToClient<SOCK> removeClient(String sysKey, String key){
		return getSys(sysKey).remove(key);
	} 
	private ToClient<SOCK> addClient(String sysKey, ToClient<SOCK> toClient){
		String key = getNewKey(sysKey);
		toClient.setKey(key);
		toClient.setSysKey(sysKey);
		getSys(sysKey).put(key, toClient);
		return toClient;
	}
	private ToClient<SOCK> addServer(String sysKey, ToClient<SOCK> toClient){
		toClient.setKey(DEFAULT_KEY);
		toClient.setSysKey(sysKey);
		getSys(sysKey).put(DEFAULT_KEY, toClient);
		return toClient;
	}
	
	private HashMap<String, ToClient<SOCK>> getSys(String sysKey){
		if(toClients.get(sysKey) == null){	//若该系统没有人登录 或者没有初始化 则初始化系统容器
			toClients.put(sysKey, new HashMap<String, ToClient<SOCK>>());
		}
		return toClients.get(sysKey);
	}
	private ToClient<SOCK> getClient(String sysKey, String key){
		return getSys(sysKey).get(key);
	}
	/**
	 * 通过底层socket引用对象来获取hashmap中的toClient(sysKey, key, socket)实例
	 */
	private ToClient<SOCK> getClient(SOCK obj){
		ToClient<SOCK> res = null;
		for(String sysKey : toClients.keySet()){
			for(String key : toClients.get(sysKey).keySet()){
				if(toClients.get(sysKey).get(key).like(obj)){
					res = toClients.get(sysKey).get(key);
				}
				
			}
		}
		return res;
	}
	//服务器 路由状态
	public String show(){
		String res = "\n\n";
		res += "\n###############################################";
		res += "\n##sysKeys : " + toClients.keySet().size();
		for(String sysKey : toClients.keySet()){
			res += "\n################";
			res += "\n##" + sysKey + " : " + toClients.get(sysKey).keySet().size();
			for(String key : toClients.get(sysKey).keySet()){
				res += "\n##" + key + "  " + toClients.get(sysKey).get(key).toString();
				
			}
		}
		res += "\n###########################################\n\n";
		out(res);
		return res;
	}

	@Override
	public void out(Object...objects) {
		Tools.out(objects);
	}

	
	
} 