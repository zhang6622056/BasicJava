package chapter22;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class SimplePoolTest {


    public static void main(String[] args) {
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>(200000);
        SimplePool simplePool = new SimplePool(blockingQueue,20);



        while(true){
            simplePool.execute(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }




}
