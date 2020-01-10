package another.semaphore;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;




/**
 *
 * 用限流器实现一个对象池
 * @author Nero
 * @date 2020-01-10
 * *@param: null
 * @return 
 */
public class ObjectPool<T> {

    private List<T> pool;
    private Semaphore semaphore;



    public ObjectPool(int size,T... t) {
        semaphore = new Semaphore(size);
        pool = new Vector<>();
        for (int i = 0 ; i < t.length ; i++){
            pool.add(t[i]);
        }
    }




    //- 从对象池内获取对象
    public void doObj() {
        T curObj = null;
        try{
            //-  允许配置的多个线程进入临界区，否则，排队等待
            semaphore.acquire();

            //- 注意，这里可以允许多个线程同时进入临界区。逻辑要求是线程安全的。
            curObj = pool.remove(0);
            System.out.println("线程id:"+Thread.currentThread().getId()+"进入到了限流器内,占用对象："+curObj);
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            pool.add(curObj);
            semaphore.release();
        }
    }


    public static void main(String[] args) {
        ObjectPool objectPool = new ObjectPool(5,"1","2","3","4","5");

        for (int i = 0 ; i < 20 ; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    objectPool.doObj();
                }
            }).start();
        }
    }

}

