package util.socket.netty;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * <pre>
 *  
 * 自己定义的协议 
 *  数据包格式 
 * +——----——+——-----——+——----——+ 
 * |  长度             |   数据       | 
 * +——----——+——-----——+——----——+ 
 * 2.传输数据的长度contentLength，int类型 
 * 3.要传输的数据,长度不应该超过2048，防止socket流的攻击
 * </pre>
 */
public class NettyDecoder extends ByteToMessageDecoder {

	/**
	 * <pre>
	 *  
	 * 表示数据的长度contentLength，int类型，占据4个字节.
	 * </pre>
	 */
	public final int BASE_LENGTH = 4;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		// 可读长度必须大于基本长度
		if (buffer.readableBytes() >= BASE_LENGTH) {

			int beginReader = buffer.readerIndex(); // 记录包头开始的index
			buffer.markReaderIndex(); // 标记包头开始的index

			int length = buffer.readInt(); // 消息的长度
			// 判断请求数据包数据是否到齐
			if (buffer.readableBytes() < length) { // 还原读指针
				buffer.readerIndex(beginReader);
				return;
			}

			byte[] data = new byte[length];
			buffer.readBytes(data); // 读取data数据

			out.add(new String(data, "UTF-8"));
		}
	}

}
