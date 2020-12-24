package threadpool;

import sun.nio.ch.ThreadPool;

import java.util.concurrent.*;

public class FixedTest {


    //
    public static void main(String[] args) throws InterruptedException {

        int corePoolSize = 10;
        int maximumPoolSize = 20;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>(50);
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("fill-parallel");

                //- TODO-ZL 异常处理稍后
                t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){

                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        System.out.println("eeeee");
                    }
                });
                return t;
            }
        };
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,1,TimeUnit.MINUTES,workQueue,threadFactory,new ThreadPoolExecutor.CallerRunsPolicy());
        UserBean userBean = new UserBean();


        Callable callableWithRes = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                if (true){
                    userBean.setUsername("123");
                    System.out.println("664455");
                    //throw new Exception("123123");
                }
                return String.valueOf("有结果的返回...");
            }
        };



        try{
            Future<String> stringFuture = threadPoolExecutor.submit(callableWithRes);
            Thread.sleep(5000L);
            stringFuture.get();
            System.out.println(userBean.getUsername());
        }catch(Exception e){
            System.out.println(e);
        }
    }



}
