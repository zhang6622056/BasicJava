package example.simplerpc.client;

import com.alibaba.fastjson.JSON;
import example.simplerpc.remote.Invocation;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;




/***
 *
 * 客户端启动动态代理
 * @author Nero
 * @date 2020-04-02
 * *@param: null
 * @return 
 */
public class ClientRpcProxy {



    public static Object create(Class clazz){
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //- 创建一个调用对象
                Invocation invocation = new Invocation();
                invocation.setArgs(null);
                invocation.setMethodName("getUsername");
                invocation.setClassName("example.simplerpc.service.UserServiceImpl");
                invocation.setInterfaceName("example.simplerpc.service.UserService");
                System.out.println(JSON.toJSONString(invocation));


                //- 业务handler
                ClientSocketNettyHendler clientSocketNettyHendler = new ClientSocketNettyHendler();




                //- 创建一个线程组
                EventLoopGroup eventExecutors=new NioEventLoopGroup();
                Bootstrap bootstrap=new Bootstrap();
                try {
                    bootstrap.group(eventExecutors)    //设置线程组
                            .channel(NioSocketChannel.class) //设置使用SocketChannel为管道通信的底层实现
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    ChannelPipeline pipeline = socketChannel.pipeline();
                                    pipeline.addLast("encoder",new ObjectEncoder());
                                    pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));//
                                    pipeline.addLast(clientSocketNettyHendler);
                                }
                            });

                    ChannelFuture future = bootstrap.connect("127.0.0.1", 9090).sync();
                    future.channel().writeAndFlush(invocation).sync();
                    future.channel().closeFuture().sync();
                    System.out.println("......client init......");
                }catch (Exception e){
                    e.printStackTrace();
                }
                return clientSocketNettyHendler.getResponse();
            }
        });
    }




}
