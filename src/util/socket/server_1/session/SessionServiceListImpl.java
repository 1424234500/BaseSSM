package util.socket.server_1.session;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.netty.channel.ChannelHandlerContext;

/**
 * 用戶连接管理
 *
 */
public class SessionServiceListImpl<T> implements SessionService<T> {
    private static class SingletonFactory<T>{           
	    private static List<Session<T>> connections = new CopyOnWriteArrayList<>();
    }

	@Override
	public void sessionAdd(T socket) {
		
	}

	@Override
	public void sessionRemove(T socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionOnRead(T socket, Object object) {
		// TODO Auto-generated method stub
		
	}

    
    
    
    
    
    
}
