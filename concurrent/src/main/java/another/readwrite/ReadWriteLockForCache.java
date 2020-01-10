package another.readwrite;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * 读写锁实现一个高性能的缓存
 * 适合于读多写少的场景
 * @author Nero
 * @date 2020-01-10
 * @param: null
 * @return 
 */
public class ReadWriteLockForCache<K,V> {


    private Map<K,V> cache;
    private ReadWriteLock readWriteLock;
    private Lock readLock;
    private Lock writeLock;





    public ReadWriteLockForCache() {
        cache = new HashMap<>();
        readWriteLock = new ReentrantReadWriteLock();

        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }



    public void write(K key,V value){
        writeLock.lock();
        try{
            cache.put(key,value);
        }finally{
            writeLock.unlock();
        }
    }



    public V read(K key){
        V value = null;

        readLock.lock();
        try{
            value = cache.get(key);
        }finally{
            readLock.unlock();
        }
        return value;
    }

}
