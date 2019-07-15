package chapter15;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;




/***
 * Lock Condition对应线程操作
 * wait - await
 * signal - notify
 * signalAll - notify
 *
 *
 * 功能描述
 * @author Nero
 * @date 2019-07-15
 * @return
 */
public class BlockedQueue<T> {

    private final Lock lock = new ReentrantLock();
    //-条件变量，队列不满
    private final Condition notFull = lock.newCondition();
    //-条件变量，队列不空
    private final Condition notEmpty = lock.newCondition();


    private int forCondition = 1;


    //-入队操作
    void inputQueue(T x){
        lock.lock();
        try{
            //队列已满，无法入队，等待
            while(forCondition == 1){
                notFull.await();
            }

            //队列未满，通知队列
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }


    //-出队操作
    void outQueue(T x){
        lock.lock();

        try{
            //队列为空，无法出队
            while(forCondition == 1){
                notEmpty.await();
            }

            //
            notFull.signal();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }
}
