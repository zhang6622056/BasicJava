package chapter19;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/***
 * CyclicBarrier 实践
 * -与CountDownLatch类似，它使得线程之间互相牵制。互相等待，直到完全就绪。
 * -这比较适合worker线程之间互相有数据依赖的场景。
 *
 *
 * await 方法 ， 一次await 将使得构造中的size减1，直到减到0
 * reset 方法 一次reset方法将size重新恢复到初始值   todo
 *
 *
 *
 *
 * @author Nero
 * @date 2019-07-15
 * @param: null
 * @return
 */
public class CyclicBarrierExam {

    final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);


    public static void main(String[] args) {
        CyclicBarrierExam cyclicBarrierExam = new CyclicBarrierExam();
        cyclicBarrierExam.race();
    }


    public void race(){
        Thread Racer1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("racer1完成了比赛。。。");

            }
        });


        Thread Racer2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("racer2完成了比赛。。。");
            }
        });
        Racer1.start();
        Racer2.start();
        cyclicBarrier.reset();
    }
}
