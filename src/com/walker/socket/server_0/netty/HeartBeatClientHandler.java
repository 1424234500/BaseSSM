package com.walker.socket.server_0.netty;  
  
import java.util.Date;

import com.walker.common.util.Tools;

import io.netty.buffer.ByteBuf;  
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;  
import io.netty.channel.ChannelInboundHandlerAdapter;  
import io.netty.handler.timeout.IdleState;  
import io.netty.handler.timeout.IdleStateEvent;  
import io.netty.util.CharsetUtil;  
import io.netty.util.ReferenceCountUtil;  
  
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {  
  
    
//    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",  CharsetUtil.UTF_8));  
//      
//    private static final int TRY_TIMES = 3;  
//      
//    private int currentTime = 0;  
    
    public void out(Object...objects){
    	Tools.out("心跳", Tools.objects2string(objects));
    }
     
    @Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
        out("循环触发时间："+new Date());  
        Channel channel=ctx.channel();
        if (evt instanceof IdleStateEvent){
            IdleStateEvent e= (IdleStateEvent) evt;
            if (e.state()== IdleState.READER_IDLE){
                channel.close();  //call back channelInactive(ChannelHandlerContext ctx)
                out(channel.remoteAddress(), "No data was received for a while ,read time out... ...");
            } //because we are attaching  more importance to the read_idle time(timeout to rec)
            else  if (e.state()== IdleState.WRITER_IDLE){ // No data was sent for a while.
                //channel.close();
                out(channel.remoteAddress(), "No data was sent for a while.write time out... ...");
            }
        }
    }   
}  