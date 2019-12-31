package chapter22;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 使用Java线程池
 * @author Nero
 * @date 2019-07-17
 * *@param: null
 * @return
 */
public class JavaPoolTest {

    public static void main(String[] args) {
        //存储任务的队列，超过任务上线将被拒绝。线程池的核心，生产消费
        BlockingQueue blockingQueue = new LinkedBlockingQueue(100);
        //自定义线程生成规则
        MyThreadFactory myThreadFactory = new MyThreadFactory();
        //自定义拒绝策略
        MyRejectedExecutionHandler myRejectedExecutionHandler = new MyRejectedExecutionHandler();


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,20,1, TimeUnit.SECONDS,blockingQueue,myThreadFactory,myRejectedExecutionHandler);

        for (int i = 0 ; i < 5000 ; i++){
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    Thread t = Thread.currentThread();

                    System.out.println(t.getName()+"..consumed....");


                    try {
                        //休眠5s，以容器角度触发线程池拒绝
                        t.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }




    /***
     *
     * 自定义自己的线程命名
     * @author Nero
     * @date 2019-07-17
     * *@param: null
     * @return
     */
    static class MyThreadFactory implements ThreadFactory {
        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"TestThread-"+mThreadNum.getAndIncrement());
        }
    }


    /*
     *
     * 自定义拒绝策略
     * @author Nero
     * @date 2019-07-17
     * *@param: null
     * @return 
     */
    static class MyRejectedExecutionHandler implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println(r.toString()+"rejected...");
        }
    }

}
