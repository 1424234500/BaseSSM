package util.socket.server_1.netty.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import util.Tools;
import util.socket.server_1.session.*;

	/**
	 * netty的handler
	 * 负责处理新旧连接
	 * 监控新消息读取
	 * @author ThinkPad
	 *
	 */
	public class SessionHandler extends ChannelInboundHandlerAdapter {
		private static SessionService<ChannelHandlerContext> sessionService = new SessionServiceListImpl<ChannelHandlerContext>();
		private class SocketNettyImpl extends Socket<ChannelHandlerContext>{
			public SocketNettyImpl(ChannelHandlerContext socket) {
				super(socket);
			}

			@Override
			public String key() {
				//ChannelHandlerContext(SessionHandler#0, [id: 0x9a9c3c84, L:/127.0.0.1:8092 - R:/127.0.0.1:34612])
				String ss[] = this.socket.toString().split(" ");
				//R:/127.0.0.1:34612
				String key = ss[ss.length - 1];	
				//127.0.0.1:34612
				key = key.substring(3);
				out(key);
//				key = this.socket.toString();
				return key;
			}

			@Override
			public void send(Object obj) {
				this.socket.writeAndFlush(obj);
			}
			
		}
		
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
			
			sessionService.sessionAdd(new SocketNettyImpl(ctx));
		}
	
		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
			super.handlerRemoved(ctx);
			out("handlerRemoved", ctx);
			
			sessionService.sessionRemove(new SocketNettyImpl(ctx));

		}
	
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) {
			out("channelRead", msg); 
			
			sessionService.sessionOnRead(new SocketNettyImpl(ctx), msg);
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