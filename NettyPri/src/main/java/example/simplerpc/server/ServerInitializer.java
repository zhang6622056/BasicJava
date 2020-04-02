package example.simplerpc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;





/***
 *
 * Rpc Initializer
 * @author Nero
 * @date 2020-04-02
 * *@param: null
 * @return 
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("encoder",new ObjectEncoder());
        pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
        //将自己编写的服务器端的业务逻辑处理类加入pipeline链中
        pipeline.addLast(ServerSocketNettyHendler.serverSocketNettyHendler);
    }
}
