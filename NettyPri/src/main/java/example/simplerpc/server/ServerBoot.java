package example.simplerpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerBoot {


    public static void main(String[] args) {
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        ServerBootstrap serverBootstrap=new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup,workerGroup)  //设置两个线程组
                    .channel(NioServerSocketChannel.class)   //设置使用NioServerSocketChannel作为服务器通道的实现
                    .option(ChannelOption.SO_BACKLOG,128) //设置线程队列中等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//保持活动连接状态
                    .childHandler(new ServerInitializer());
            ChannelFuture future = serverBootstrap.bind(9090).sync();
            future.channel().closeFuture().sync();
            System.out.println(".........server start..........");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }



}
