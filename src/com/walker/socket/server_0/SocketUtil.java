package com.walker.socket.server_0;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.walker.common.util.Tools;

public class SocketUtil {
	
/**
 * 包协议
 * 4个字节 长度 size 
 * 正文\r
 * 	
 */
	
	
	/**
	 * socket io 阻塞模式读取
	 */
	public static String readImpl(Socket socket, InterfaceOut interfaceOut) throws Exception {
		String res = "";
		InputStream is = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		
		if(isr.ready()){
			byte[] head = new byte[4];
	        int read = is.read(head, 0, head.length);	//尝试读取数据流 的头4个字节<int> 读取长度 -1表示读取到了数据流的末尾了；
	        if (read != -1) {
	            int size = Tools.bytes2int(head);	//头4个字节 int 大小 int = 4byte = 32bit
	            int readCount = 0;
	            StringBuilder sb = new StringBuilder();
	            while (readCount < size) {  //读取已知长度消息内容 异步读取 死循环 直到读够目标字节数
		            byte[] buffer = new byte[2048];
	                read = is.read(buffer, 0, Math.min(size - readCount, buffer.length) );
	                if (read != -1) {
	                    readCount += read;
                        sb.append(new String(buffer,"UTF-8"));
	                }
	            }
	            res = sb.toString();
	            interfaceOut.out("size", size, "res", res);
	        } 
		}
		return res;
	}

	/**
	 * socket io 阻塞模式发送
	 */
	public static void sendImpl(Socket socket, String jsonstr, InterfaceOut socketIO) throws Exception {
		if(!Tools.notNull(jsonstr))return;
		byte[] bytes = jsonstr.getBytes();
		OutputStream os = socket.getOutputStream();
		os.write(Tools.int2bytes(bytes.length));	//int = 4byte = 32bit
		os.write(bytes);
//		os.write('\r');
//		os.write('\n');
		os.flush();		
	}

	
	
	
	/**
	 * socket nio 非阻塞模式发送字节包
	 */
	public static void sendImpl(SocketChannel socket, String jsonstr, InterfaceOut socketIO) throws Exception {
		if(!Tools.notNull(jsonstr))return;
		
		byte[] bytes = jsonstr.getBytes("UTF-8");
        int size = bytes.length;
        ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
        sizeBuffer.putInt(bytes.length);
        sizeBuffer.flip();
        while (sizeBuffer.hasRemaining()) {
            socket.write(sizeBuffer);
        }	
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.put(bytes);
        buffer.flip();
        while (buffer.hasRemaining()) {
            socket.write(buffer);
        }	
        socket.finishConnect();
	}
	/**
	 * socket nio 非阻塞模式读取字节包
	 */
	public static String readImpl(SocketChannel socket, InterfaceOut interfaceOut) throws Exception {
		String res = "";
		
        ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
        sizeBuffer.clear();
        int read = socket.read(sizeBuffer);	//尝试读取数据流 的头4个字节<int> 读取长度 -1表示读取到了数据流的末尾了；
        if (read != -1) {	//若有数据<0.2条数据 1条数据 3.2条数据>
            sizeBuffer.flip();
            int size = sizeBuffer.getInt();	//头4个字节 int 大小 int = 4byte = 32bit
            
            int readCount = 0;
            byte[] b = new byte[1024];
    		ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder sb = new StringBuilder();
            while (readCount < size) {  //读取已知长度消息内容 异步读取 死循环 直到读够目标字节数
                buffer.clear();
                read = socket.read(buffer);
                if (read != -1) {
                    readCount += read;
                    buffer.flip();
                    int index = 0 ;
                    while(buffer.hasRemaining()) {
                        b[index++] = buffer.get();
                        if (index >= b.length) {
                            index = 0;
                            sb.append(new String(b,"UTF-8"));
                        }
                    }
                    if (index > 0) {
                        sb.append(new String(b,"UTF-8"));
                    }
                }
            }
            res = sb.toString();
//            interfaceOut.out("size", size, "res", res);
        } 
		return res;		
	}
	
	
	
	
	
	
	
}
