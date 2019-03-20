package util.socket.server_1;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import util.setting.Setting;
import util.socket.server_1.netty.handler.NettyDecoder;
import util.socket.server_1.netty.handler.NettyEncoder;
import util.socket.server_1.netty.handler.SessionHandler;

/**
 * 底层 socket 基本实现 负责实现socket通信 的 连接建立 并调用上层server的 事件 send receive
 * 再由上层server决定调用底层的 send receive...
 *
 */

public class SocketNetty {
	EventLoopGroup bossGroup;
	EventLoopGroup workerGroup;
	static String serverHostName; // 服务器hostname
	static String serverIp; // 服务器ip
	static int serverPort; // 服务器port
	static int cpus = Runtime.getRuntime().availableProcessors();
	
	public SocketNetty() {
		serverPort = Setting.get("socket_port_netty", 8092);
	}
 

	void start() throws Exception{
		// Configure the server.
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup(cpus);
		try {
			ServerBootstrap b = new ServerBootstrap()
			.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(
					new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
	//						p.addLast(new LoggingHandler(LogLevel.INFO));
							p.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS)); 	//5s心跳包 
	//						p.addLast( new ObjectEncoder(),  new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
						    p.addLast(new NettyEncoder(), new NettyDecoder());  
	//						p.addLast(new HeartBeatClientHandler());  
							p.addLast(new SessionHandler());
	
						}
			});

			// Start the server.
			ChannelFuture f = b.bind(serverPort).sync();

			// Wait until the server socket is closed.
			f.channel().closeFuture().sync();
		} finally {
			// Shut down all event loops to terminate all threads.
			stop();
		}

	} 
	
 
	protected void send(ChannelHandlerContext socket, String jsonstr) {
		socket.writeAndFlush(jsonstr);
	}


	protected void stop() {
		if(bossGroup != null){
			bossGroup.shutdownGracefully();
			bossGroup = null;
		}
		if(workerGroup != null){
			workerGroup.shutdownGracefully();
			workerGroup = null;
		}
	}

		
	
}
