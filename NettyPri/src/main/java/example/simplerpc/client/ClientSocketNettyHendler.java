package example.simplerpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;




/***
 *
 * 调用端监听返回事件
 * @author Nero
 * @date 2020-04-02
 * *@param: null
 * @return 
 */
public class ClientSocketNettyHendler extends ChannelInboundHandlerAdapter {

    private Object response;

    public Object getResponse(){
        return response;
    }

    //读取数据事件
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.response=msg;
        ctx.close();
    }
}
