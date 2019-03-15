package util.socket.server_1.session;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.netty.channel.ChannelHandlerContext;

/**
 * 用戶连接管理
 *
 */
public class SessionMgr {
    private static class SingletonFactory{           
	    private static List<Session<ChannelHandlerContext>> connections = new CopyOnWriteArrayList<>();
    }

    
    
    
    
    
    
}
