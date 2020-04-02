package example.chatroom.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class TestServerHandler extends SimpleChannelInboundHandler<String> {

    //- 聊天室group
    private static ChannelGroup group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"上线\n");
        System.out.println(group.size());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //- 先发送消息，再加入，以保证上线的人不收到该消息
        group.writeAndFlush(channel.remoteAddress()+"加入\n");
        group.add(channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel channel = channelHandlerContext.channel();
        group.forEach(ch -> {
            //- 不发给自己，发送给group 其他人
            if (ch != channel){
                ch.writeAndFlush(channel.remoteAddress()+"："+s+"\r\n");
            }
        });

        //- 继续向下一个handler流转，如果是最后一个handler则不需要这句话
        channelHandlerContext.fireChannelRead(s);
    }



    //- 发生异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }




    //- 下线，自动触发
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"下线\n");
    }




    //- channel下线，group自动移除。
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        group.writeAndFlush(channel.remoteAddress()+"离开\n");
    }
}
