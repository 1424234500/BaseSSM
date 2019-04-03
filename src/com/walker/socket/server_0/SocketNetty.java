package com.walker.socket.server_0;

import com.walker.common.setting.Setting;
import com.walker.socket.server_0.netty.NettyDecoder;
import com.walker.socket.server_0.netty.NettyEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 底层 socket 基本实现 负责实现socket通信 的 连接建立 并调用上层server的 事件 send receive
 * 再由上层server决定调用底层的 send receive...
 *
 */

public class SocketNetty extends SocketFrame<ChannelHandlerContext> {
	EventLoopGroup bossGroup;
	EventLoopGroup workerGroup;
	static String serverHostName; // 服务器hostname
	static boolean boolIfOn; // 是否开启监听线程
	static String serverIp; // 服务器ip
	static int serverPort; // 服务器port

	public SocketNetty() {
		serverPort = Setting.get("socket_port_netty", 8092);
	}
 

	@Override
	protected void startImpl() throws Exception {
		// Configure the server.
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(
					new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
//						p.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS)); 	//5s心跳包 
//						p.addLast(new LoggingHandler(LogLevel.INFO));
//						p.addLast( new ObjectEncoder(),  new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
					    p.addLast(new NettyEncoder(), new NettyDecoder());  
//						p.addLast(new HeartBeatClientHandler());  
						p.addLast(new HandlerNetty());

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
	

	@Override
	protected void startRead(ChannelHandlerContext socket) throws Exception {
	}


	@Override
	protected String readImpl(ChannelHandlerContext socket) throws Exception {
		return "";
	}


	@Override
	protected void sendImpl(ChannelHandlerContext socket, String jsonstr) throws Exception {
		socket.writeAndFlush(jsonstr);
	}


	@Override
	protected void stopImpl() {
		if(bossGroup != null){
			bossGroup.shutdownGracefully();
			bossGroup = null;
		}
		if(workerGroup != null){
			workerGroup.shutdownGracefully();
			workerGroup = null;
		}
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
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			super.userEventTriggered(ctx, evt);
			out("userEventTriggered", ctx, evt);
			
		
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);
//			out("channelActive", ctx);
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			super.channelInactive(ctx);
			out("channelInactive", ctx);
		}

		@Override
		public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
			super.handlerAdded(ctx);
			onNewConnection(ctx);
//			out("handlerAdded", ctx);
		}
	
		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
			super.handlerRemoved(ctx);
			onDisConnection(ctx);
//			out("handlerRemoved", ctx);
		}
	
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) {
			onReceive(ctx, (String)msg);
//			out("channelRead", msg); 
		}
	
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) {
			ctx.flush();
//			out("channelReadComplete", ctx);
		}
	
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			// Close the connection when an exception is raised.
			ctx.close();
			out("异常", ctx, cause);
			
		}
	
	}
	
	
}
