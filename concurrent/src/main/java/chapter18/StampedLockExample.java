package chapter18;


import java.util.concurrent.locks.StampedLock;

/***
 * 支持三种锁模式
 * -写锁
 * -悲观读锁
 * -乐观读
 *
 * 注意事项：
 * -不可重入
 * -读，写都不支持条件变量
 * -如果线程阻塞在readLock或者WriteLock上的话，这时候调用线程Interrupt方法会导致cpu飙升到100%，使用StampedLock一定不要调用中断操作
 *
 * 支持锁的升级与降级,但是要谨慎使用
 * @author Nero
 * @date 2019-07-15
 * *@param: null
 * @return 
 */
public class StampedLockExample {

    private int x,y;

    final StampedLock stampedLock = new StampedLock();

    double distanceFromOrigin(){
       //乐观读
       long stamp = stampedLock.tryOptimisticRead();
       //读入局部变量
       //读的过程数据可能被修改
       int curX = x,curY = y;


       //-验证不通过，读的期间存在写操作
       if (!stampedLock.validate(stamp)){
           stamp = stampedLock.readLock();

            try{
                curX = x;
                curY = y;
            }finally{
                stampedLock.unlockRead(stamp);
            }
       }
        return Math.sqrt(curX * curX + curY * curY);
    }
}
