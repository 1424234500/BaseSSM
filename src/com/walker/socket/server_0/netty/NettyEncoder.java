package com.walker.socket.server_0.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/** 
	 * <pre> 
	 *  数据包格式 
	 * +——----——+——-----——+——----——+ 
	 * |  ^		 长度      |   数据  | 
	 * +——----——+——-----——+——----——+ 
	 * 1.起始符号^
	 * 2.传输数据的长度contentLength，int类型 
	 * 3.要传输的数据 {asdfasdf}
	 * 
	 */  
	public class NettyEncoder extends MessageToByteEncoder<String> {  
	  
	    @Override  
	    protected void encode(ChannelHandlerContext tcx, String jsonstr, ByteBuf out) throws Exception {  
	    	byte[] bs = jsonstr.getBytes(NettyDecoder.ENCODE);
	    	
	        // 1.写入消息的开头的信息标志  
	    	out.writeChar(NettyDecoder.START_WITH);
	    	// 2.写入长度
//	    	out.writeBytes(NettyDecoder.int2bytes(bs.length));
	        out.writeInt(bs.length);  
	        // 3.写入消息的内容(byte[]类型)  
	        out.writeBytes(bs);  
	    }  
	}  