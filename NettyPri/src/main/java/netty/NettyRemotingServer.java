package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyRemotingServer {


    private final ServerBootstrap serverBootstrap;


    //- 用于管理链接的组
    private final EventLoopGroup bossGroup;
    //- 用于处理逻辑事件的组
    private final EventLoopGroup workerGroup;











    public NettyRemotingServer() {
        this.serverBootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup(4);
    }


    public void start(){






        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new InitHandler()).bind(1009).sync();

    }



    class InitHandler extends ChannelInitializer {

        protected void initChannel(Channel channel) throws Exception {
            ChannelPipeline channelPipeline = channel.pipeline();
            channelPipeline.addLast("",handler);
            channelPipeline.addLast("",handler);

        }
    }






    public static void main(String[] args) {
        NettyRemotingServer nettyRemotingServer = new NettyRemotingServer();



    }






}