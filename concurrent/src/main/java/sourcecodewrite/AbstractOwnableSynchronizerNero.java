package sourcecodewrite;

import java.io.Serializable;

public class AbstractOwnableSynchronizerNero implements Serializable {

    /** Use serial ID even though all fields transient. */
    private static final long serialVersionUID = 3737899427754241962L;


    protected AbstractOwnableSynchronizerNero() { }

    //- 当前持有锁的线程缓存
    private transient Thread exclusiveOwnerThread;


    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }



}
