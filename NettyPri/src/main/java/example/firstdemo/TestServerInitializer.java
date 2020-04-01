package example.firstdemo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;




/***
 *
 * HttpRequestEncoder，将HttpRequest或HttpContent编码成ByteBuf
 * HttpRequestDecoder，将ByteBuf解码成HttpRequest和HttpContent
 * HttpResponseEncoder，将HttpResponse或HttpContent编码成ByteBuf
 * HttpResponseDecoder，将ByteBuf解码成HttpResponse和HttpContent
 *
 * handler处理器编辑辅助类
 * @author Nero
 * @date 2020-04-01
 * *@param: null
 * @return 
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //- 配置handler
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast("testServerHandler",new TestServerHandler());
    }
}
