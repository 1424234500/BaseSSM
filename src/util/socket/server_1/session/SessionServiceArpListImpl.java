package util.socket.server_1.session;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

import util.Bean;
import util.FileUtil;
import util.Fun;
import util.JsonUtil;
import util.pipe.Pipe;
import util.pipe.PipeMgr;
import util.pipe.PipeMgr.Type;
import util.route.SubPub;
import util.route.SubPubMgr;
import util.socket.server_1.*;
import util.socket.server_1.filter.*;
import util.socket.server_1.plugin.*;

/**
 * 用戶连接管理
 * 策略：key相同的session 是否需要覆盖socket对象 ？
 * 是否存在key相同 而 socket对不上 已销毁？
 * 
 * 会话管理 消息传递路由 逻辑key 用户id
 * 具体消息由用户自己解决处理业务
 * 
 * 模拟arp rarp ip mac路由转换报文
 * 
 */
public class SessionServiceArpListImpl<T> implements SessionService<T> {
	private static Logger log = Logger.getLogger(SessionServiceArpListImpl.class); 
    

	/**
	 * 会话列表 Arp 	mac:<mac,ip>
	 */
    private Map<String, Session<T>> index = new ConcurrentHashMap<>();
    
    /**
     * 业务处理队列 消费
     */
    private Pipe<MsgUp> pipe = PipeMgr.getPipe(Type.PIPE, "session");

    @SuppressWarnings("unchecked")
	public SessionServiceArpListImpl(){
    	String str = FileUtil.readByLines(ClassLoader.getSystemResource("").getPath() + "plugin.json", null);
		Bean bean = JsonUtil.get(str);
		PluginFactory.init((Bean)bean.get("plugins"));	
		FilterFactory.init((List<Bean>)bean.get("filters"));	
		
		
    	pipe.startConsumer(1, new Fun<MsgUp>() {
			public Object make(MsgUp msg) {
				Session<T> session = index.get(msg.getFrom());
				if(session != null) {
					NDC.push(session.toString());
					try {
						if(FilterFactory.doFilter(session, msg) && msg.getType().length() > 0) {
							PluginFactory.doPlugin(session, msg);
						}
					}catch(Exception e) {
						log.error(e.toString(), e);
						//插件处理异常 实时反馈异常
						session.send(MsgBuilder.makeException(session, msg, e));
					}
					NDC.pop();
				}else {
					log.error("该用户已不存在 " + msg.toString());
				}
				return true;
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
			session = new Session<T>(socket);
			session.onConnect();
			index.put(key, session);
			log.debug("add user " + session);
		}
	}
	@Override
	public void sessionRemove(Socket<T> socket) {
		String key = socket.key();
		Session<T> session = index.get(key);
		if(session != null) {
			session.onUnConnect();
			index.remove(key);
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
			sessionCheck(session, socket);
			sessionData(session, msg);		//所有数据处理
		}else {//异常请求
			log.error("receive msg from no user ? " + socket);
			socket.send("receive msg from no user ? " + socket);
		}
	}
	/**
	 * 校验 session-socket和socket是否相同
	 */
	private void sessionCheck(Session<T> session, Socket<T> socket) {
		if(!session.socket.equals(socket)) {
			log.error("check error f->t " + session + " " + socket);
		}
	}
	/**
	 * 某会话发来某消息 
	 * 
	 * 封装完整的 msg from to data 放入队列缓存消费 先后顺序 排队处理 并针对特定类型消息优先处理
	 * 线程读取能力（只负责io，同时解析为msg对象，设置些上下文信息） 和 业务处理能力（线程消费） 是否需要队列缓冲 （互相干扰）
	 * 
	 * 所有收到的消息 都归属于server 
	 * 
	 */
	private void sessionData(Session<T> session, Object msgJsonStr) {
		MsgUp msg = new MsgUp(msgJsonStr.toString(), session);
		pipe.put(msg);
	}
    
	
	
    
    
    
} 
