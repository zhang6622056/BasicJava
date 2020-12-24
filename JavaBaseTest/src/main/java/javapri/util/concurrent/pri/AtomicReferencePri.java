package javapri.util.concurrent.pri;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferencePri {

    public static void main(String[] args) throws InterruptedException {
        AtomicReference<Integer> atomicReference = new AtomicReference<Integer>(0);
        System.out.println(atomicReference.get());

        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(100);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,100,10, TimeUnit.SECONDS,linkedBlockingQueue);


        for (int i = 0 ; i < 100 ; i++){
            final int temp = i;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    AtomicReference<Integer> tempAtomicReference = new AtomicReference<>(temp);
                    boolean isRunAgagin = true;
                    while(isRunAgagin){
                        if (atomicReference.compareAndSet(temp,temp+1)){
                            System.out.println(atomicReference.get());
                            isRunAgagin = false;
                        }
                    }
                }
            });
        }

        Thread.sleep(10000);
        System.out.println(atomicReference.get());
    }




}
