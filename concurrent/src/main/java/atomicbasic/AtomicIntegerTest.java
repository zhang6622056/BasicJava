package atomicbasic;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class AtomicIntegerTest {


    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int res = atomicInteger.get();

        boolean setres = atomicInteger.compareAndSet(res,res+1);
        System.out.println(atomicInteger.get());
    }









    class Worker extends AbstractQueuedSynchronizer implements Runnable{

        private Runnable task;
        private Thread thread;


        public Worker(Runnable task) {
            this.task = task;
        }




        @Override
        public void run() {

        }
    }
}
