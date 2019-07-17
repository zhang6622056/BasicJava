package chapter23;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/***
 *
 * FutureTask 初体验，最优烧水泡茶程序
 * cover 王宝令老师
 *
 * t1:洗水壶 -> 烧开水 -> t2.done -> 泡茶
 * t2:洗茶壶 -> 洗茶杯 -> 拿茶叶
 *
 *
 * @author Nero
 * @date 2019-07-17
 * *@param: null
 * @return 
 */
public class FutureTaskInit {


    public static void main(String[] args) {


        //-任务2：洗茶壶 -> 洗茶杯 -> 拿茶叶
        Callable<Boolean> taskTwo = new Callable() {
            @Override
            public Object call() throws Exception {
                Thread.currentThread().sleep(5000);
                return true;
            }
        };
        FutureTask<Boolean> futureTask2 = new FutureTask<>(taskTwo);

        //任务1：洗水壶 -> 烧开水 -> t2.done -> 泡茶
        Callable<String> taskOne = new Callable<String>(){

            @Override
            public String call() throws Exception {
                System.out.println("洗水壶 over....");
                System.out.println("烧开水 over....");

                //关键点,阻塞等待任务2完成,这里也可以用CountDownLatch实现
                futureTask2.get();
                System.out.println("泡茶 over....");

                return "泡茶成功...";
            }
        };

        FutureTask<String> futureTask1 = new FutureTask<>(taskOne);



        //2个线程并行执行任务
        new Thread(futureTask1).start();
        new Thread(futureTask2).start();


        try {
            String res = futureTask1.get();
            System.out.println(res);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
