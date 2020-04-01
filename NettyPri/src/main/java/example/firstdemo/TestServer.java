package example.firstdemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 *
 * netty server的模版类
 * @author Nero
 * @date 2020-04-01
 * *@param: null
 * @return
 */
public class TestServer {


    public static void main(String[] args) {
        //- 接受客户端链接的线程组，一般都是指定为1个
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //- 真正处理读写事件的线程组，cpu核心数x2
        EventLoopGroup workGroup = new NioEventLoopGroup();

        //- 创建netty启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)  //- 服务断所使用的通道
                .childHandler(new TestServerInitializer());    //- 读写事件pipeline，用来添加链式处理器
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }










}
