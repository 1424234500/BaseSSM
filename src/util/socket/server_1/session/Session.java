package util.socket.server_1.session;

public class Session<SOCK> {

	
	//同时建立索引在数据结构中ServerImpl
	String sysKey;	//所属系统
	String key;		//客户编号
	
	SOCK socket;

	
	public boolean like(SOCK sock){
	return this.socket == sock;
	}
	
	
	public String getSysKey() {
	return sysKey;
	}
	
	
	
	public void setSysKey(String sysKey) {
	this.sysKey = sysKey;
	}
	
	
	
	public String getKey() {
	return key;
	}
	
	
	
	public void setKey(String key) {
	this.key = key;
	}
	
	
	
	public SOCK getSocket() {
	return socket;
	}
	
	
	
	public void setSocket(SOCK socket) {
	this.socket = socket;
	}
	
	
	
	@Override
	public String toString() {
	return "ToClient [sysKey=" + sysKey + ", key=" + key + ", socket=" + socket + "]";
	}

}
