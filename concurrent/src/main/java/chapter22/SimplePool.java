package chapter22;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/***
 *
 * 自己写一个简单的线程池
 * - 用BlockingQueue 存储Runnable 可执行的任务对象
 * - 用List存储线程数量
 * - 构造传入线程池容器
 *
 *
 * @author Nero
 * @date 2019-07-16
 * @return
 */
public class SimplePool {

    //利用阻塞队列实现生产者-消费者模式
    BlockingQueue<Runnable> workQueue;

    //保存内部工作线程
    List<WorkerThread> threads = new ArrayList<>();

    public SimplePool(BlockingQueue<Runnable> workQueue,int size) {
        for (int i = 0 ; i < size ; i++){
            WorkerThread workerThread = new WorkerThread();
            threads.add(workerThread);
            workerThread.start();
        }
        this.workQueue = workQueue;
    }

    public void execute(Runnable command){
        try {
            workQueue.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    /***
     *
     * 消费任务
     * @author Nero
     * @date 2019-07-16
     * @return
     */
    class WorkerThread extends Thread{
        @Override
        public void run() {
           while(true){
               try {
                   System.out.println(Thread.currentThread().getId());
                   Thread.currentThread().sleep(5000);
                   Runnable runnable = workQueue.take();
                   runnable.run();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        }
    }



}
