package util.socket.server_1.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import util.Tools;

	/**
	 * netty的handler
	 * 负责处理新旧连接
	 * 监控新消息读取
	 * @author ThinkPad
	 *
	 */
	public class SessionHandler extends ChannelInboundHandlerAdapter {
		
		
		
		public void out(Object...objects) {
			Tools.out(objects);
		}
		
		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			super.userEventTriggered(ctx, evt);
			out("userEventTriggered", ctx, evt);
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);
			out("channelActive", ctx);
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			super.channelInactive(ctx);
			out("channelInactive", ctx);
		}

		@Override
		public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
			super.handlerAdded(ctx);
			out("handlerAdded", ctx);
			
			
			
		}
	
		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
			super.handlerRemoved(ctx);
			out("handlerRemoved", ctx);
			
			
		}
	
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) {
			out("channelRead", msg); 
			
			
		}
	
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) {
			ctx.flush();
			out("channelReadComplete", ctx);
		}
	
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			// Close the connection when an exception is raised.
			ctx.close();
			out("exceptionCaught", ctx, cause);
		}
	
	}