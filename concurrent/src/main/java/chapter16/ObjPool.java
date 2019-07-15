package chapter16;



import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;



/***
 * 利用 Semaphore 实现多线程限流器
 * Semaphore 可以理解为线程容器，可以同时允许N个线程进入临界区，而N则为构造的参数size
 * acquire 获取到锁，进入临界区，消耗一个容量
 * release 释放锁，离开临界区，释放一个容量
 *
 * @author Nero
 * @date 2019-07-15
 * @return 
 */
public class ObjPool<T,R> {


    final List<T> pool;
    final Semaphore semaphore;


    public ObjPool(int size,T t) {
        pool = new Vector<T>();
        for (int i = 0 ; i < size ; i++){
            pool.add(t);
        }
        this.semaphore = new Semaphore(size);
    }



    /**
     *
     * 开始执行
     * @author Nero
     * @date 2019-07-15
     * @return void
     */
    public void exec(){
        try {
            //获取可执行权，占用信号量
            semaphore.acquire();
            System.out.println("获取到了信号量的执行权。。。");
            Thread.sleep(5000);
            //值得注意的是，这里是多个线程可以同时进入的，需要保证临界区的操作也是线程安全的。
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            //释放线程占用信号量
            semaphore.release();
        }
    }
}
