package util.socket;

import java.util.HashMap;

import util.Tools;

/**
 * 
 * 
 */

public abstract class ServerImpl implements Server{
			//系统key       系统子级key  客户端引用
	HashMap<String, HashMap<String, ToClient>> toClients = null;
//	<0, <0, obj>> this default
//	<0, <1001, obj>> this 未登录1
//	<0, <1002, obj>> this 未登录2

//	<10a0, <0,    obj>> 10a0服务器-this 
//	<10a0, <1001, obj>> 10a0服务器-1 
	
	
	
	@Override
	public void send(String sysKey, String key, Msg msg) {
		Tools.out("send", sysKey, key, msg);

		ToClient tc = this.getClient(sysKey, key);
		
	}
	//从某个系统  某个客户 发来的msg(对本系统的操作key, map转发数据包)
	@Override
	public void onReceive(String sysKey, String key, Msg msg) {
		Tools.out("onReceive", sysKey, key, msg);
		
		//某个刚连接的用户需要 认证系统 默认归类到0 当前系统列 从当前系统移到新系统并生成新key 
		if(msg.key == Msg.LOGIN_CLIENT){	//客户端登录
			String newSysKey = (String) msg.get("syskey", "0");
			this.changeClient(sysKey, key, newSysKey);
		}else if(msg.key == Msg.LOGIN_SERVER){//服务器登录
			String newSysKey = (String) msg.get("syskey", "0");
			String pwd = (String) msg.get("pwd", "0");
			this.changeServer(sysKey, key, newSysKey);
		}else if(msg.key == Msg.TOCLIENT){//转发消息到客户端
			this.send(sysKey, key, msg);
		}else if(msg.key == Msg.TOSERVER){//转发消息到服务器
			this.send(sysKey, "0", msg);
		}else if(msg.key == Msg.TOCLIENT){//转发消息到客户端
			this.send(sysKey, key, msg);
		}
		
	}

	
	@Override
	public <Arg> void onNewConnection(Arg obj) {
		//新连接 添加为自己掌管的客户 socket
		String sysKey = "0";
		ToClient toClient = new ToClient();
		this.addClient(sysKey, toClient);
	}
	@Override
	public boolean start() {
		toClients = new HashMap<>();
		
		this.onStart("OK");
		return true;
	}

	@Override
	public boolean pause() {
		return true;
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		this.onStop("OK");
		return true;
	}

	@Override
	public <T> void make(T obj) {
	}
 

	//获取某系统下一个最小的不重复的编码
	private String getNewKey(String sysKey){
		HashMap<String, ToClient> sys = getSys(sysKey);
		String res = "";
		int start = 1000;
		for(String key : sys.keySet()){
			if(!key.equals(start++ +""))
				break;
		}
		res = start + "";	
		return res;
	}
	
	

	public ToClient changeServer(String sysKey, String key, String toSysKey){
		return this.addServer(toSysKey, this.removeClient(sysKey, key));
	}
	public ToClient changeClient(String sysKey, String key, String toSysKey){
		return this.addClient(toSysKey, this.removeClient(sysKey, key));
	}
	public ToClient removeClient(String sysKey, String key){
		return getSys(sysKey).remove(key);
	}
	public ToClient addClient(String sysKey, ToClient toClient){
		getSys(sysKey).put(getNewKey(sysKey), toClient);
		return toClient;
	}
	public ToClient addServer(String sysKey, ToClient toClient){
		getSys(sysKey).put("0", toClient);
		return toClient;
	}
	
	private HashMap<String, ToClient> getSys(String sysKey){
		if(toClients.get(sysKey) == null){	//若该系统没有人登录 或者没有初始化 则初始化系统容器
			toClients.put(sysKey, new HashMap<String, ToClient>());
		}
		return toClients.get(sysKey);
	}
	private ToClient getClient(String sysKey, String key){
		return getSys(sysKey).get(key);
	}
} 