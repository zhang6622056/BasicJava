package example.heartbeat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TestClient {

    public static void main(String[] args) {
        EventLoopGroup bossGroup=new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new TestClientInitializer());

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",9090);
    }




}
