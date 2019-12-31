package threadpool;

import java.util.concurrent.*;

public class FixedTest {


    //
    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(5);
        while(true){
            System.out.println(blockingQueue.offer("1"));
            Thread.sleep(500);
        }









        //ExecutorService executorService = Executors.newFixedThreadPool(10);




    }



}
