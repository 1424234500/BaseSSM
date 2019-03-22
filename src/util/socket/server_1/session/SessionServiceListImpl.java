package util.socket.server_1.session;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import util.Fun;
import util.pipe.Pipe;
import util.pipe.PipeMgr;
import util.pipe.PipeMgr.Type;
import util.route.SubPub;
import util.route.SubPubMgr;
import util.socket.server_1.Msg;
import util.socket.server_1.MsgBuilder;

/**
 * 用戶连接管理
 * 策略：key相同的session 是否需要覆盖socket对象 ？
 * 是否存在key相同 而 socket对不上 已销毁？
 * 
 * 会话管理 消息传递路由 逻辑key 用户id
 * 具体消息由用户自己解决处理业务
 * 
 * 拆出登录请求 此时无用户id 单独处理登录拦截
 *
 */
public class SessionServiceListImpl<T> implements SessionService<T> {
	private static Logger log = Logger.getLogger(SessionServiceListImpl.class); 
	/**
	 * 会话列表
	 */
    private Map<String, Session<T>> index = new ConcurrentHashMap<>();
    /**
     * 业务处理队列 消费
     */
    private Pipe<Object> pipe = PipeMgr.getPipe(Type.PIPE, "session");
    /**
     * 路由 发布订阅
     */
    private SubPub<Msg> pub = SubPubMgr.getSubPub(0);
    /**
     * 过滤器配置
     */
//    private List<Filter> filters = ArrayList<Filter>();
    
    public SessionServiceListImpl(){
    	
    	
    	pipe.startConsumer(1, new Fun<Object>() {
			public Object make(Object obj) {
				Msg msg = new Msg(obj.toString());
				Set<String> tos = msg.getTo();
				for(String to : tos) {
					pub.publish(to, msg);
				}
				return null;
			}
    	});
    	
    }
	@Override
	public void sessionAdd(Socket<T> socket) {
		String key = socket.key();
		Session<T> session = index.get(key);
		if(session != null) {
			sessionCheck(session, socket);
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
			sessionCheck(session, socket);
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
				sessionCheck(session, socket);
				sessionFilter(session, msg);	//登录请求则登录 否则异常 其他过滤 否则异常 
				sessionData(session, msg);		//正常数据处理 
			}catch(Exception e) {
				log.error(e.toString(), e);
				socket.send(MsgBuilder.getException(e));
			}
		}else {//异常请求
			log.error("receive msg from no user ? " + socket);
			socket.send(new Msg().setOk(false).setInfo("receive msg from no user ? " + socket));
		}
	}
	/**
	 * 校验 session-socket和socket是否相同
	 */
	private void sessionCheck(Session<T> session, Socket<T> socket) {
		if(session.socket.equals(socket)) {
			log.error("check error f->t " + session + " " + socket);
		}
	}
	/**
	 * 过滤socket 拦截
	 * 过滤链
	 * 配置化
	 * 
	 * 未登录请求只能容许登录例外
	 * 		例外 登录请求
	 * 
	 * @return 
	 * 
	 */
	private boolean sessionFilter(Session<T> session, Object msg) {
		
		
		return true;
	}
	/**
	 * 某会话发来某消息 
	 * 
	 * 封装完整的 msg from to data 放入队列缓存消费 先后顺序 排队处理 并针对特定类型消息优先处理
	 * 线程读取能力 和 业务处理能力 是否需要队列缓冲 （互相干扰）
	 * 
	 * 所有收到的消息 都归属于server 
	 * 
	 */
	private void sessionData(Session<T> session, Object msgJsonStr) {
//		msg.setFromSession(session);
		pipe.put(msgJsonStr);
	}
    
	
	
    
    
    
} 
