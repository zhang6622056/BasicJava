package threadpool;

import java.util.concurrent.*;

public abstract class BaseFill {


    void fill(UserBean userBean){
        userBean.setUsername("BaseFill");
       System.out.println("BaseFill");
   }

   void fillStart(UserBean userBean){
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
       ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,1, TimeUnit.MINUTES,workQueue,threadFactory,new ThreadPoolExecutor.CallerRunsPolicy());


       Callable callableWithRes = new Callable<Object>() {
           @Override
           public Object call() throws Exception {
               if (true){
                   fill(userBean);
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
