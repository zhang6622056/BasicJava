package chapter24;


import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/***
 *
 * 功能描述
 *
 * 默认CompletableFuture采用的是fork/join线程池，线程个数与cpu的线程数相同
 * 如果线程内部堆积了大量的io操作，那么可能会导致线程阻塞。
 * 建议不同的业务用不同的线程池。
 * 可以通过-Djava.util.concurrent.ForkJoinPool.common.parallelism 参数来设置Forkjoin线程池大小
 *
 * @author Nero
 * @date 2019-07-17
 * *@param: null
 * @return 
 */
public class Chapter24 {

    /***
     *
     * 用CompletableFuture  协调线程关系，执行顺序
     * @author Nero
     * @date 2019-07-17
     * *@param:
     * @return void
     */
    public void RunWithCompletableFuture(){


        //无返回异步任务,洗水壶，烧开水
        CompletableFuture f1 = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                System.out.println("T1 洗水壶。。。");

                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("T1 烧开水。。。");

                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });






        //洗茶壶，洗茶杯子，拿茶叶
        CompletableFuture f2 = CompletableFuture.supplyAsync(new Supplier<Object>() {
            @Override
            public Object get() {
                System.out.println("T2 洗茶壶...");
                System.out.println("T2 洗茶杯子...");
                System.out.println("T2 拿茶叶...");
                return "龙井";
            }
        });


        //-顾名思义 在f1和f2之后执行匿名函数f3
        CompletableFuture f3 = f1.thenCombine(f2,(__, f2res)->{
            System.out.println("T3 泡茶...");
            return f2res;
        });

        f3.join();
        System.out.println("泡茶成功....");
    }


    public static void main(String[] args) {
        Chapter24 chapter24 = new Chapter24();
        chapter24.RunWithCompletableFuture();
    }
}
