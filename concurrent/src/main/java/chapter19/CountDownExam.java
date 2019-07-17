package chapter19;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



/**
 *
 * 借由countDownLatch 实现瞬间并发
 *
 * countDownLatch 作为引用计数，经常搭配线程池使用，因为线程池内没有wait，notify方法
 * -countDown 方法，将countdown的计数-1，直到countdown构造传入的参数全部减完，则不再wait
 * -await 将程序wait在指定位置。等待所有的引用计数减完。直到0
 *
 *
 *
 * @author Nero
 * @date 2019-07-15
 * *@param: null
 * @return 
 */
public class CountDownExam {

    final CountDownLatch  countDownLatch = new CountDownLatch(21);
    Executor executor = Executors.newFixedThreadPool(20);


    public void testCountDown(){
        int i = 0;
        while(i <= 20){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //等待countdownLatch对象的计数全部调用countDown方法完成之后，再执行下面的逻辑
                        countDownLatch.countDown();
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("并发输出。。。。");
                }
            });
            i++;
        }



        countDownLatch.countDown();

    }


    public static void main(String[] args) {
        new CountDownExam().testCountDown();
    }





}
