package example.heartbeat.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class TestServerLnitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //针对客户端，如果1分钟之内没有向服务器发送读写心跳，则主动断开
        pipeline.addLast(new IdleStateHandler(40,50,45));
        //自定义的读写空闲状态检测
        pipeline.addLast(new HeartBeatHandler());
    }
}