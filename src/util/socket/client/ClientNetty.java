package util.socket.client;
 
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;
import util.RobotUtil;
import util.Tools;
import util.setting.Setting;
import util.socket.SocketNetty;
import util.socket.SocketUtil;
import util.socket.SocketNetty.HandlerNetty;
import util.socket.netty.NettyDecoder;
import util.socket.netty.NettyEncoder;

public class ClientNetty extends ClientFrame {
	ChannelHandlerContext socket;
	String serverIp = "127.0.0.1";
	int serverPort = 8091; 
	
	ClientNetty(){
		serverPort = Setting.getInt("socket_port_netty", 8092);
		try {
			serverIp = Setting.getString("socket_ip", Tools.getServerIp(InetAddress.getLocalHost().getHostAddress()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		serverIp = Setting.getString("socket_ip", "127.0.0.1");
	}
	ClientNetty(String ip, int port){
		this.serverIp = ip;
		this.serverPort = port;
	}
	
	
	@Override
	public String show() {
		return socket.toString();
	}

	@Override
	protected void startRead() throws Exception {
		read();
	}

	@Override
	protected String readImpl() throws Exception {
//		return SocketUtil.readImpl(socket, this);
		return "";
	}

	@Override
	protected void sendImpl(String jsonstr) throws Exception {
		socket.writeAndFlush(jsonstr);
	}

	@Override
	protected void startImpl() throws Exception {
		out("Netty", serverIp, serverPort);
		  // Configure the client.  
        EventLoopGroup group = new NioEventLoopGroup();  
        try {  
            Bootstrap b = new Bootstrap();  
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)  
                .handler(new ChannelInitializer<SocketChannel>() {  
                    @Override  
                    public void initChannel(SocketChannel ch) throws Exception {  
						ChannelPipeline p = ch.pipeline();
//						p.addLast("ping", new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS)); 	//5s心跳包 
//						p.addLast(new LoggingHandler(LogLevel.INFO));
//						p.addLast( new ObjectEncoder(),  new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
					    p.addLast(new NettyEncoder(), new NettyDecoder());  
						p.addLast(new HandlerNetty());

                    }  
                });  
  
            // Start the client.  
            ChannelFuture f = b.connect(serverIp, serverPort).sync();  
  
            // Wait until the connection is closed.  
            f.channel().closeFuture().sync();  
        }  
        finally {  
            // Shut down the event loop to terminate all threads.  
            group.shutdownGracefully();  
        }  
	}

	@Override
	protected void stopImpl() {
		 
	} 
	
	
	
	
	
	
	
	

	
	/**
	 * netty的handler
	 * 负责处理新旧连接
	 * 监控新消息读取
	 * @author ThinkPad
	 *
	 */
	public class HandlerNetty extends ChannelInboundHandlerAdapter {
		 
		
		@Override
		public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
			super.handlerAdded(ctx);
			socket = ctx;
//			out("handlerAdded", ctx);
			out("连接成功", ctx.toString());
		}
	
		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
			super.handlerRemoved(ctx);
//			out("handlerRemoved", ctx);
			out("断开连接", ctx);
		}
	
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) {
//			onReceive(ctx, msg.toString());
//			out("channelRead", msg);
			out("收到", msg);
		}
	
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) {
			ctx.flush();
//			out("channelReadComplete", ctx);
		}
	
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			// Close the connection when an exception is raised.
			cause.printStackTrace();
			ctx.close();
//			out("exceptionCaught", ctx, cause);
			out("异常", cause);
		}
	
	}
	
		
  
}
