package com.walker.socket.server_1;

import java.util.concurrent.TimeUnit;

import com.walker.common.setting.Setting;
import com.walker.socket.server_1.netty.handler.NettyDecoder;
import com.walker.socket.server_1.netty.handler.NettyEncoder;
import com.walker.socket.server_1.netty.handler.SessionHandler;

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
		int t = Setting.get("netty_thread", 1);
		bossGroup = new NioEventLoopGroup(t); // (1)
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) // (3)
//         	 .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                	//过滤器 编码解码 心跳 会话业务 依次处理
					ChannelPipeline p = ch.pipeline();
//					p.addLast(new LoggingHandler(LogLevel.INFO));
					p.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS)); 	//5s心跳包 
//					p.addLast( new ObjectEncoder(),  new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
				    p.addLast(new NettyEncoder(), new NettyDecoder());  
//					p.addLast(new HeartBeatClientHandler());  
					p.addLast(new SessionHandler());                 
				}
             })
             .option(ChannelOption.SO_BACKLOG, 128)          // (5)
             .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
    
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(serverPort).sync(); // (7)
    
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
			// Shut down all event loops to terminate all threads.
			stop();
        }
	} 

	protected void stop() {
		if(bossGroup != null){
			bossGroup.shutdownGracefully();
		}
		if(workerGroup != null){
			workerGroup.shutdownGracefully();
		}
	}

		
	
}
