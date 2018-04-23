package util.socket.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import util.Tools;

/** 
	 * <pre> 
	 *  数据包格式 
	 * +——----——+——-----——+——----——+ 
	 * |  长度          |   数据       | 
	 * +——----——+——-----——+——----——+ 
	 * 2.传输数据的长度contentLength，int类型 
	 * 3.要传输的数据 
	 * </pre> 
	 */  
	public class NettyEncoder extends MessageToByteEncoder<String> {  
	  
	    @Override  
	    protected void encode(ChannelHandlerContext tcx, String jsonstr, ByteBuf out) throws Exception {  
	        // 1.写入消息的开头的信息标志(int类型)  
	    	byte[] bs = jsonstr.getBytes();
	    	out.writeBytes(Tools.int2bytes(bs.length));
//	        out.writeInt(bs.length);  
	        // 3.写入消息的内容(byte[]类型)  
	        out.writeBytes(bs);  
	    }  
	}  