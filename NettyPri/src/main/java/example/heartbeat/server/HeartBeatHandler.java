package example.heartbeat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    /**
     * 用户事件触发的处理器
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //判断evt是否属于IdleStateEvent，用于触发用户事件，包含读空闲，写空闲，读写空闲
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if(event.state() == IdleState.READER_IDLE){
                //读空闲，不做处理
                System.out.println("进入读空闲");
            }else if(event.state() == IdleState.WRITER_IDLE){
                //写空闲，不做处理
                System.out.println("进入写空闲");
            }else if(event.state() == IdleState.ALL_IDLE){
                //关闭channel
                Channel channel = ctx.channel();
                System.out.println("单位时间内未上报，服务端断开....");
                channel.close();
            }

        }
    }
}
