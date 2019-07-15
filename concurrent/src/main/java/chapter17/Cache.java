package chapter17;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;



/***
 * 在读层面放开了锁的限制，只锁写操作，适合读多，少写的情况，
 * 由此减少了锁的粒度
 *
 * 读写锁不支持升级，但是支持降级。
 * 锁升级:获取读锁以后，没有释放就尝试获取写锁，会阻塞线程
 * 锁降级:获取写锁以后，获取读锁是允许的
 * 只有写锁支持条件变量队列，读锁不支持条件变量队列
 *
 *
 * @author Nero
 * @date 2019-07-15
 * *@param: null
 * @return 
 */
public class Cache<K,V> {
    //-HashMap 不是线程安全的，这里用readWriteLock 保证线程安全
    final Map<K,V> cache = new HashMap<>();
    final ReadWriteLock rwl = new ReentrantReadWriteLock();
    final Lock writeLock = rwl.writeLock();
    final Lock readLock = rwl.readLock();



    /**
     *
     * 插入缓存
     * @author Nero
     * @date 2019-07-15
     * @param: k
     * @param: v
     * @return void
     */
    public void put(K k,V v){
        writeLock.lock();
        try{
            cache.put(k,v);
        }finally{
            writeLock.unlock();
        }
    }


    /***
     *
     * 高并发的情况下要判断2次，第二次用写锁loading
     * @author Nero
     * @date 2019-07-15
     * @param: k
     * @return V
     */
    public V get(K k){
        readLock.lock();
        V v = null;
        try{
            v = cache.get(k);
        }finally{
            readLock.unlock();
        }
        if(null != v) {return v;}



        //防止其他线程已经读到缓存中，再次验证，这次用写锁加锁，保证只有一个线程去load
        writeLock.lock();
        try{
            //ready from db
        }finally{
            writeLock.unlock();
        }
        return v;
    }

}
