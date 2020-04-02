package example.seconddemo.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //- maxFrameLength 表示包的最大长度，超出的部分，netty将做一些特殊处理
        //- lengthFieldOffset： 描述长度数据开始的偏移量
        //- lengthFieldLength：从lengthFieldOffset 开始，接下来lengthFieldLength长度为描述消息体长度的数据。
        //- lengthAdjustment：该字段加长度字段等于数据帧的长度，包体长度调整的大小，长度域的数值表示的长度加上这个修正值表示的就是带header的包；
        //- initialBytesToStrip：从数据帧中跳过的字节数，表示获取完一个完整的数据包之后，忽略前面的指定的位数个字节，应用解码器拿到的就是不带长度域的数据包；
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        pipeline.addLast(new LengthFieldPrepender(4)); //计算当前待发送消息的二进制字节长度，将该长度添加到ByteBuf的缓冲区头中


        //- StringDecoder 将bytebuf转成string，继续向下流转
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8)); //ChannelInboundHandlerAdapter

        //- 将string转成bytebuf，out流转
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));//ChannelOutboundHandlerAdapter

        //- 业务处理类
        pipeline.addLast(new TestServerHandler());
    }
}
