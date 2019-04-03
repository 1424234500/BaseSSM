package com.walker.socket.server_1.netty.handler;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 自己定义的协议 
 *  数据包格式 
 *  ^020F{data:alksdjfka}
 * 1.开始符号 ^，1个字节
 * 2.传输数据的长度contentLength，int类型，4个字节
 * 3.要传输的数据,长度不应该超过2048
 */
public class NettyDecoder extends ByteToMessageDecoder {

	/**
	 * 包头长度
	 */
	public final static int BASE_LENGTH = 5;
	public final static char START_WITH = '^';
	public final static String ENCODE = "UTF-8";
	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。 和bytesToInt2（）配套使用
	 */
	public static byte[] int2bytes(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (value >> (24 - i * 8));
		}
		return b;
	}

	public static int bytes2int(byte[] b) {
		return (((int) b[0] << 24) + (((int) b[1]) << 16) + (((int) b[2]) << 8) + b[3]);
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		
		int len = buffer.readableBytes();
		while(len > 0) {
			int beginReader = buffer.readerIndex(); // 记录包头开始的index
			buffer.markReaderIndex(); // 标记包头开始的index
			char startWith = buffer.readChar(); //开始标识
			if(startWith == START_WITH) { //非开始符号 丢弃 移位
				if(len+1 > BASE_LENGTH) { //有有效数据 加上头标示 ^0020{xxx}
					int length = buffer.readInt(); // 取消息的长度
					// 判断请求数据包数据是否到齐
					if (buffer.readableBytes() < length) { // 还原读指针
						buffer.readerIndex(beginReader);
					}else {
						byte[] data = new byte[length];
						buffer.readBytes(data); // 读取data数据
						out.add(new String(data, ENCODE));
					}
				}else {	//无有效数据 还原
					buffer.readerIndex(beginReader);
				}
				return;
			}
			len --;
		}
		 
	}

}
