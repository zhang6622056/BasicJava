package chapter15;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * 课后作业为，线程条件为了均衡每个线程的请求时间。signalAll，不应该是signal，否则会有很多超时
 * Condition表示一个条件队列，当前队列调用的await相当于thread.wait 这就是concurrent 包实现管程的方式
 *
 *
 * dubbo实现异步转同步
 * @author Nero
 * @date 2019-07-15
 * *@param: null
 * @return
 */
public class DubboSourceForSync {
    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();


    /****
     * 方法实现了异步转同步,同步不断的通知条件队列，唤醒等待线程重新请求
     * DefaultFuture.get
     * @author Nero
     * @date 2019-07-15
     * *@param: timeout
     * @return java.lang.Object
     */
    Object get(int timeout){
        long start = System.nanoTime();
        lock.lock();

        try{
            //数据还没有返回，则挂起一段时间
            while(!isDone()){
                done.await(timeout, TimeUnit.NANOSECONDS);
                long cur = System.nanoTime();

                if (isDone()){
                    //执行完成，唤醒所有等待队列中的线程
                    done.signalAll();
                    break;
                }
                if(cur - start > timeout){
                    done.signalAll();
                    //超时，唤醒所有等待队列中的线程
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
        return null;
    }


    //RPC 结果是否已经返回
    boolean isDone(){
        return false;
    }
}
