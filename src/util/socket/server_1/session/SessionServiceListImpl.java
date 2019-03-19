package util.socket.server_1.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * 用戶连接管理
 * 策略：key相同的session 是否需要覆盖socket对象 ？
 * 是否存在key相同 而 socket对不上 已销毁？
 *
 */
public class SessionServiceListImpl<T> implements SessionService<T> {
	private static Logger log = Logger.getLogger(SessionServiceListImpl.class); 
    private Map<String, Session<T>> index = new ConcurrentHashMap<>();
    
	@Override
	public void sessionAdd(Socket<T> socket) {
		String key = socket.key();
		Session<T> session = index.get(key);
		if(session != null) {
			checkSocket(session, socket);
			log.error("add user have exist?" + key + " " + session);
		}else {
			session = new Session<>(socket);
			index.put(key, session);
			log.debug("add user " + session);
		}
		
	}
	@Override
	public void sessionRemove(Socket<T> socket) {
		String key = socket.key();
		if(index.containsKey(key)) {
			Session<T> session = index.remove(key);
			checkSocket(session, socket);
			log.debug("add user " + session);
		}else {
			log.error("remove no user " + key + " " + index.get(key));
		}		
	}
	@Override
	public void sessionOnRead(Socket<T> socket, Object msg) {
		String key = socket.key();
		Session<T> session = index.get(key);
		if(session != null) {
			try {
				checkSocket(session, socket);
				filterSession(session, msg);
				session.onData(msg);
			}catch(Exception e) {
				log.error(e.toString(), e);
				socket.send(e.toString());
			}
		}else {
			log.error("receive msg from no user ? " + socket);
		}
		
		
	}
	/**
	 * 校验 session-socket和socket是否相同
	 */
	private void checkSocket(Session<T> session, Socket<T> socket) {
		if(session.socket.equals(socket)) {
			log.error("check error f->t " + session + " " + socket);
		}
	}
	/**
	 * 过滤socket
	 * 过滤链
	 * 配置化
	 * 
	 */
	private void filterSocket(Session<T> session, Object msg) {
		
		
	}
    
    
    
    
}
