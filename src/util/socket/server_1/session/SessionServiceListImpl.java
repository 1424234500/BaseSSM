package util.socket.server_1.session;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import util.Bean;
import util.FileUtil;
import util.Fun;
import util.JsonUtil;
import util.pipe.Pipe;
import util.pipe.PipeMgr;
import util.pipe.PipeMgr.Type;
import util.route.SubPub;
import util.route.SubPubMgr;
import util.socket.server_1.Msg;
import util.socket.server_1.MsgBuilder;
import util.socket.server_1.MsgException;
import util.socket.server_1.plugin.Plugin;
import util.socket.server_1.plugin.PluginFactory;

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
    private Pipe<Msg> pipe = PipeMgr.getPipe(Type.PIPE, "session");
    /**
     * 路由 发布订阅
     */
    private SubPub<Msg> pub = SubPubMgr.getSubPub("msg_route", 0);
    /**
     * 业务处理器配置
     */
    private Bean plugins;
    private List<Filter> filters = new ArrayList<Filter>();

    public SessionServiceListImpl(){
    	String str = FileUtil.readByLines(ClassLoader.getSystemResource("").getPath() + "plugin.json", null);
		Bean bean = JsonUtil.get(str);
		plugins = (Bean)bean.get("plugins");
		PluginFactory.init(plugins);	
			
    	pipe.startConsumer(1, new Fun<Msg>() {
			@SuppressWarnings("unchecked")
			public Object make(Msg msg) {
				try {
					String type = msg.getType();

					//过滤器链处理 过滤登录 例外 filter
					
					//按类别处理 业务 plugin  存储 加工 发送socket
					Plugin plugin = PluginFactory.getPlugin(type);
					if(plugin != null) {
						plugin.onData(msg.getData());
					}
				}catch(MsgException e) {
					log.error(e.toString(), e);
					Session<T> session = index.get(msg.getKeyFrom());
					session.send(MsgBuilder.getException(e));
				}
				return null;
				
				//发送一条消息给服务器
				//
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
				sessionData(session, msg);		//所有数据处理
			}catch(MsgException e) {
				log.error(e.toString(), e);
				session.send(MsgBuilder.getException(e));	//回执异常
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
	 * 某会话发来某消息 
	 * 
	 * 封装完整的 msg from to data 放入队列缓存消费 先后顺序 排队处理 并针对特定类型消息优先处理
	 * 线程读取能力（只负责io，同时解析为msg对象，设置些上下文信息） 和 业务处理能力（线程消费） 是否需要队列缓冲 （互相干扰）
	 * 
	 * 所有收到的消息 都归属于server 
	 * 
	 */
	private void sessionData(Session<T> session, Object msgJsonStr) {
		Msg msg = new Msg(msgJsonStr.toString());
		msg.setKeyFrom(session.getKey());	//设置来源socket key session<T>
		pipe.put(msg);
	}
    
	
	
    
    
    
} 
