package chapter21;

import java.util.concurrent.atomic.AtomicLong;

public class Atomic {



    AtomicLong atomicLong = new AtomicLong(1000);

    public void longUse(){
        atomicLong.addAndGet(Long.valueOf(1000));
        atomicLong.compareAndSet(atomicLong.get(),2000l);
    }



}
