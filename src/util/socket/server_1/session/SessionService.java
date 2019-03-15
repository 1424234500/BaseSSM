package util.socket.server_1.session;

public interface SessionService<T> {

	void sessionAdd(T socket);
	
	void sessionRemove(T socket);
	
	void sessionOnRead(T socket, Object object);
	
	
	
	
	
}
