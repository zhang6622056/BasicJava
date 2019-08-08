package objectmemory;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;





/****
 *
 * 模拟了锁，实现了从轻量级到重量级锁的变更。
 * 但是依然对标志位存在质疑
 * @author Nero
 * @date 2019-08-08
 * *@param: null
 * @return 
 */
public class FirstDisplay {
    private static final Lock lock = new ReentrantLock();
    private static final Integer a = Integer.MAX_VALUE;
    private static final ExecutorService excutor = Executors.newFixedThreadPool(2);


    public static void lockForPrint(){
        synchronized (a){
            System.out.println(ClassLayout.parseInstance(a).toPrintable());
        }
    }





    public static void main(String[] args) {

        System.out.println(ClassLayout.parseInstance(a).toPrintable());
        System.out.println("---------------------");


        for (int i = 0 ; i < 3 ; i++){
            excutor.execute(new Runnable() {
                @Override
                public void run() {
                    lockForPrint();
                }
            });
        }
        excutor.shutdownNow();
    }


}
