package example.simplerpc.server;

import example.simplerpc.remote.Invocation;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@ChannelHandler.Sharable
public class ServerSocketNettyHendler extends ChannelInboundHandlerAdapter {


    private static ExecutorService executorService= Executors.newFixedThreadPool(200);
    public static final ServerSocketNettyHendler serverSocketNettyHendler=new ServerSocketNettyHendler();

    private ServerSocketNettyHendler() {
    }




    //读取数据事件
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Invocation invocation= (Invocation) msg;
                    Object o = Class.forName(getImplClassName(invocation)).newInstance();
                    Method method = o.getClass().getMethod(invocation.getMethodName(), invocation.getParametersType());
                    Object invoke = method.invoke(o, invocation.getArgs());
                    ctx.channel().writeAndFlush(invoke);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }






    //得到某个接口下的实现类
    public String getImplClassName(Invocation invocation) throws Exception {
        //服务器接口与实现类地址;
        String interfaceName = invocation.getInterfaceName();
        Class aClass = Class.forName(interfaceName);


        String packageName = interfaceName.substring(0,interfaceName.lastIndexOf("."));
        Reflections reflections=new Reflections(packageName);
        Set<Class<?>> classes=reflections.getSubTypesOf(aClass);
        if(classes.size()==0){
            System.out.println("未找到实现类");
            return null;
        }else if(classes.size()>1){
            System.out.println("找到多个实现类，未明确使用哪个实现类");
            return null;
        }else{
            Class[] classes1 = classes.toArray(new Class[0]);
            return classes1[0].getName();
        }
    }





    public static void main(String[] args) throws ClassNotFoundException {
        String iname = "example.simplerpc.service";
        String className="UserService";
        Class aClass = Class.forName(iname + "."+className);
        Reflections reflections=new Reflections(iname);
        Set<Class<?>> classes=reflections.getSubTypesOf(aClass);
        System.out.println(classes.size());
    }





}
