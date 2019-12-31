package sourcecodewrite;

import sun.misc.Unsafe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public class AbstractQueuedSynchronizerNero extends AbstractOwnableSynchronizerNero {


    protected AbstractQueuedSynchronizerNero() {
    }


    static final class Node{


        // 共享模式的标记
        static final Node SHARED = new Node();
        // 独占模式的标记
        static final Node EXCLUSIVE = null;

        //-线程被取消
        static final int CANCELLED =  1;
        //-后继线程(即队列中此节点之后的节点)需要被阻塞.(用于独占锁)
        static final int SIGNAL    = -1;
        //-线程在Condition条件上等待阻塞.(用于Condition的await等待)
        static final int CONDITION = -2;
        //-下一个acquireShared方法线程应该被允许。(用于共享锁)
        static final int PROPAGATE = -3;

        //- 当前节点的状态，默认状态为0，
        volatile int waitStatus;

        //- 双向链表
        volatile Node prev;
        volatile Node next;

        //- 该节点存储的线程
        volatile Thread thread;


        Node nextWaiter;
        final boolean isShared() {
            return nextWaiter == SHARED;
        }


        //- 返回前一个node
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }


        Node() {}

        // Used by addWaiter
        Node(Thread thread,Node mode) {
            this.nextWaiter = mode;
            this.thread = thread;
        }

        // Used by Condition
        Node(Thread thread, int waitStatus) {
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }



    private transient volatile Node head;
    private transient volatile Node tail;


    //- 线程状态锁，如果没有线程获取到锁则state为0，否则>0
    private volatile int state;


    protected final int getState() {
        return state;
    }
    protected final void setState(int newState) {
        state = newState;
    }



    /***
     *
     * cas获取锁，将state设置为>0
     * @author Nero
     * @date 2019-12-04
     * @param: expect
     * @param: update
     * @return boolean
     */
    protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }


    static final long spinForTimeoutThreshold = 1000L;



    /**
     * 把node放到最尾部
     * @author Nero
     * @date 2019-12-04
     * *@param: node
     * @return sourcecodewrite.AbstractQueuedSynchronizerNero.Node
     */
    private AbstractQueuedSynchronizerNero.Node enq(final AbstractQueuedSynchronizerNero.Node node) {
        for (;;) {
            AbstractQueuedSynchronizerNero.Node t = tail;
            if (t == null) {
                if (compareAndSetHead(new AbstractQueuedSynchronizerNero.Node()))
                    tail = head;
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }




    /***
     *
     * 获取锁失败，一般调用此方法增加一个等待节点，比如调用lock的时候
     * @author Nero
     * @date 2019-12-04
     * @param: mode 传入的节点模式
     * @return sourcecodewrite.AbstractQueuedSynchronizerNero.Node
     */
    private AbstractQueuedSynchronizerNero.Node addWaiter(AbstractQueuedSynchronizerNero.Node mode) {
        AbstractQueuedSynchronizerNero.Node node = new AbstractQueuedSynchronizerNero.Node(Thread.currentThread(), mode);
        AbstractQueuedSynchronizerNero.Node pred = tail;

        //- 设置一次插入队尾，如果没有成功，则调用enq重复cas
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        //- 把node放到队尾
        enq(node);
        return node;
    }


    private void setHead(AbstractQueuedSynchronizerNero.Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }


    private void unparkSuccessor(AbstractQueuedSynchronizerNero.Node node) {
        int ws = node.waitStatus;
        if (ws < 0)
            compareAndSetWaitStatus(node, ws, 0);


        AbstractQueuedSynchronizerNero.Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            for (AbstractQueuedSynchronizerNero.Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
        if (s != null)
            LockSupport.unpark(s.thread);
    }

    private void doReleaseShared() {
        for (;;) {
            AbstractQueuedSynchronizerNero.Node h = head;
            if (h != null && h != tail) {
                int ws = h.waitStatus;
                if (ws == AbstractQueuedSynchronizerNero.Node.SIGNAL) {
                    if (!compareAndSetWaitStatus(h, AbstractQueuedSynchronizerNero.Node.SIGNAL, 0))
                        continue;            // loop to recheck cases
                    unparkSuccessor(h);
                }
                else if (ws == 0 &&
                        !compareAndSetWaitStatus(h, 0, AbstractQueuedSynchronizerNero.Node.PROPAGATE))
                    continue;
            }
            if (h == head)
                break;
        }
    }


    private void setHeadAndPropagate(AbstractQueuedSynchronizerNero.Node node, int propagate) {
        AbstractQueuedSynchronizerNero.Node h = head; // Record old head for check below
        setHead(node);
        if (propagate > 0 || h == null || h.waitStatus < 0 ||
                (h = head) == null || h.waitStatus < 0) {
            AbstractQueuedSynchronizerNero.Node s = node.next;
            if (s == null || s.isShared())
                doReleaseShared();
        }
    }



    /***
     *
     * 将node节点出队
     * @author Nero
     * @date 2019-12-04
     * *@param: node
     * @return void
     */
    private void cancelAcquire(AbstractQueuedSynchronizerNero.Node node) {

        if (node == null){return;}
        node.thread = null;
        AbstractQueuedSynchronizerNero.Node pred = node.prev;

        //- 如果前一节点已经为取消态，则继续向前关联
        while (pred.waitStatus > 0){node.prev = pred = pred.prev;}
        AbstractQueuedSynchronizerNero.Node predNext = pred.next;

        //- 设置当前竞争锁状态为取消态
        node.waitStatus = AbstractQueuedSynchronizerNero.Node.CANCELLED;

        //- 如果为队尾，则调整指针出队
        if (node == tail && compareAndSetTail(node, pred)) {
            compareAndSetNext(pred, predNext, null);
        } else {
            int ws;
            if (pred != head &&
                    ((ws = pred.waitStatus) == AbstractQueuedSynchronizerNero.Node.SIGNAL ||
                            (ws <= 0 && compareAndSetWaitStatus(pred, ws, AbstractQueuedSynchronizerNero.Node.SIGNAL))) &&
                    pred.thread != null) {
                AbstractQueuedSynchronizerNero.Node next = node.next;
                if (next != null && next.waitStatus <= 0)
                    compareAndSetNext(pred, predNext, next);
            } else {
                unparkSuccessor(node);
            }

            // help GC
            node.next = node;
        }
    }



    /***
     *
     * 功能描述 
     * @author Nero
     * @date 2019-12-04
     * @param: pred  pre of node
     * @param: node 竞争锁的节点
     * @return boolean
     */
    private static boolean shouldParkAfterFailedAcquire(AbstractQueuedSynchronizerNero.Node pred, AbstractQueuedSynchronizerNero.Node node) {
        int ws = pred.waitStatus;

        //- 前一个线程的waitStatus在-1状态，后继节点需要等待
        if (ws == AbstractQueuedSynchronizerNero.Node.SIGNAL){ return true;}


        //- 前一节点已经为CANCEL态。找到前继节点为非CANCEL态的节点，并设置关联
        if (ws > 0) {
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            //- 设置前继节点为-1态。
            compareAndSetWaitStatus(pred, ws, AbstractQueuedSynchronizerNero.Node.SIGNAL);
        }
        return false;
    }

    /**
     * Convenience method to interrupt current thread.
     */
    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    /**
     * Convenience method to park and then check if interrupted
     *
     * @return {@code true} if interrupted
     */
    private final boolean parkAndCheckInterrupt() {
        //- 设置线程阻塞
        LockSupport.park(this);
        //- 设置当前线程中断标志位
        return Thread.interrupted();
    }


    /***
     *
     * 获取锁失败，将当前线程排队
     * @author Nero
     * @date 2019-12-04
     * *param: node  排队的线程node
     * @param: arg   1
     * @return boolean
     */
    final boolean acquireQueued(final AbstractQueuedSynchronizerNero.Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                //- 排队优化，如果当前节点排名第二。则尝试将队列头部节点获取锁
                final AbstractQueuedSynchronizerNero.Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }

                //- 竞争锁失败，设置线程阻塞，并排队。设置中断标志位
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt()){ interrupted = true; }

            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }



    private void doAcquireInterruptibly(int arg)
            throws InterruptedException {
        final AbstractQueuedSynchronizerNero.Node node = addWaiter(AbstractQueuedSynchronizerNero.Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (;;) {
                final AbstractQueuedSynchronizerNero.Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }


    private boolean doAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L)
            return false;
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(AbstractQueuedSynchronizerNero.Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (;;) {
                final AbstractQueuedSynchronizerNero.Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return true;
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    return false;
                if (shouldParkAfterFailedAcquire(p, node) &&
                        nanosTimeout > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }


    private void doAcquireShared(int arg) {
        final AbstractQueuedSynchronizerNero.Node node = addWaiter(AbstractQueuedSynchronizerNero.Node.SHARED);
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final AbstractQueuedSynchronizerNero.Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        if (interrupted)
                            selfInterrupt();
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }



    private void doAcquireSharedInterruptibly(int arg)
            throws InterruptedException {
        final AbstractQueuedSynchronizerNero.Node node = addWaiter(AbstractQueuedSynchronizerNero.Node.SHARED);
        boolean failed = true;
        try {
            for (;;) {
                final AbstractQueuedSynchronizerNero.Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    private boolean doAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L)
            return false;
        final long deadline = System.nanoTime() + nanosTimeout;
        final AbstractQueuedSynchronizerNero.Node node = addWaiter(AbstractQueuedSynchronizerNero.Node.SHARED);
        boolean failed = true;
        try {
            for (;;) {
                final AbstractQueuedSynchronizerNero.Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return true;
                    }
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    return false;
                if (shouldParkAfterFailedAcquire(p, node) &&
                        nanosTimeout > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }

    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }

    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }


    /***
     *
     * lock方法传递进来的arg为1
     * @author Nero
     * @date 2019-12-04
     * *@param: arg
     * @return void
     */
    public final void acquire(int arg) {
        //- tryAcquire 尝试获取锁
        //- acquireQueued 获取锁失败，排队
        if (!tryAcquire(arg) &&
                acquireQueued(addWaiter(AbstractQueuedSynchronizerNero.Node.EXCLUSIVE), arg))
            selfInterrupt();
    }

    public final void acquireInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (!tryAcquire(arg))
            doAcquireInterruptibly(arg);
    }

    public final boolean tryAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquire(arg) ||
                doAcquireNanos(arg, nanosTimeout);
    }

    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            AbstractQueuedSynchronizerNero.Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }

    public final void acquireShared(int arg) {
        if (tryAcquireShared(arg) < 0)
            doAcquireShared(arg);
    }

    public final void acquireSharedInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (tryAcquireShared(arg) < 0)
            doAcquireSharedInterruptibly(arg);
    }

    public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquireShared(arg) >= 0 ||
                doAcquireSharedNanos(arg, nanosTimeout);
    }


    public final boolean releaseShared(int arg) {
        if (tryReleaseShared(arg)) {
            doReleaseShared();
            return true;
        }
        return false;
    }

    public final boolean hasQueuedThreads() {
        return head != tail;
    }

    public final boolean hasContended() {
        return head != null;
    }

    public final Thread getFirstQueuedThread() {
        // handle only fast path, else relay
        return (head == tail) ? null : fullGetFirstQueuedThread();
    }



    private Thread fullGetFirstQueuedThread() {

        AbstractQueuedSynchronizerNero.Node h, s;
        Thread st;
        if (((h = head) != null && (s = h.next) != null &&
                s.prev == head && (st = s.thread) != null) ||
                ((h = head) != null && (s = h.next) != null &&
                        s.prev == head && (st = s.thread) != null))
            return st;



        AbstractQueuedSynchronizerNero.Node t = tail;
        Thread firstThread = null;
        while (t != null && t != head) {
            Thread tt = t.thread;
            if (tt != null)
                firstThread = tt;
            t = t.prev;
        }
        return firstThread;
    }


    public final boolean isQueued(Thread thread) {
        if (thread == null)
            throw new NullPointerException();
        for (AbstractQueuedSynchronizerNero.Node p = tail; p != null; p = p.prev)
            if (p.thread == thread)
                return true;
        return false;
    }


    final boolean apparentlyFirstQueuedIsExclusive() {
        AbstractQueuedSynchronizerNero.Node h, s;
        return (h = head) != null &&
                (s = h.next)  != null &&
                !s.isShared()         &&
                s.thread != null;
    }

    public final boolean hasQueuedPredecessors() {
        // The correctness of this depends on head being initialized
        // before tail and on head.next being accurate if the current
        // thread is first in queue.
        AbstractQueuedSynchronizerNero.Node t = tail; // Read fields in reverse initialization order
        AbstractQueuedSynchronizerNero.Node h = head;
        AbstractQueuedSynchronizerNero.Node s;
        return h != t &&
                ((s = h.next) == null || s.thread != Thread.currentThread());
    }


    public final int getQueueLength() {
        int n = 0;
        for (AbstractQueuedSynchronizerNero.Node p = tail; p != null; p = p.prev) {
            if (p.thread != null)
                ++n;
        }
        return n;
    }



    public final Collection<Thread> getQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (AbstractQueuedSynchronizerNero.Node p = tail; p != null; p = p.prev) {
            Thread t = p.thread;
            if (t != null)
                list.add(t);
        }
        return list;
    }


    public final Collection<Thread> getExclusiveQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (AbstractQueuedSynchronizerNero.Node p = tail; p != null; p = p.prev) {
            if (!p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }


    public final Collection<Thread> getSharedQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (AbstractQueuedSynchronizerNero.Node p = tail; p != null; p = p.prev) {
            if (p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }


    public String toString() {
        int s = getState();
        String q  = hasQueuedThreads() ? "non" : "";
        return super.toString() +
                "[State = " + s + ", " + q + "empty queue]";
    }


    final boolean isOnSyncQueue(AbstractQueuedSynchronizerNero.Node node) {
        if (node.waitStatus == AbstractQueuedSynchronizerNero.Node.CONDITION || node.prev == null)
            return false;
        if (node.next != null) // If has successor, it must be on queue
            return true;

        return findNodeFromTail(node);
    }

    private boolean findNodeFromTail(AbstractQueuedSynchronizerNero.Node node) {
        AbstractQueuedSynchronizerNero.Node t = tail;
        for (;;) {
            if (t == node)
                return true;
            if (t == null)
                return false;
            t = t.prev;
        }
    }

    final boolean transferForSignal(AbstractQueuedSynchronizerNero.Node node) {
        if (!compareAndSetWaitStatus(node, AbstractQueuedSynchronizerNero.Node.CONDITION, 0))
            return false;


        AbstractQueuedSynchronizerNero.Node p = enq(node);
        int ws = p.waitStatus;
        if (ws > 0 || !compareAndSetWaitStatus(p, ws, AbstractQueuedSynchronizerNero.Node.SIGNAL))
            LockSupport.unpark(node.thread);
        return true;
    }


    final boolean transferAfterCancelledWait(AbstractQueuedSynchronizerNero.Node node) {
        if (compareAndSetWaitStatus(node, AbstractQueuedSynchronizerNero.Node.CONDITION, 0)) {
            enq(node);
            return true;
        }
        while (!isOnSyncQueue(node))
            Thread.yield();
        return false;
    }


    final int fullyRelease(AbstractQueuedSynchronizerNero.Node node) {
        boolean failed = true;
        try {
            int savedState = getState();
            if (release(savedState)) {
                failed = false;
                return savedState;
            } else {
                throw new IllegalMonitorStateException();
            }
        } finally {
            if (failed)
                node.waitStatus = AbstractQueuedSynchronizerNero.Node.CANCELLED;
        }
    }



    public final boolean owns(AbstractQueuedSynchronizerNero.ConditionObject condition) {
        return condition.isOwnedBy(this);
    }


    public final boolean hasWaiters(AbstractQueuedSynchronizerNero.ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.hasWaiters();
    }


    public final int getWaitQueueLength(AbstractQueuedSynchronizerNero.ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitQueueLength();
    }


    public final Collection<Thread> getWaitingThreads(AbstractQueuedSynchronizerNero.ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitingThreads();

    }

    public class ConditionObject implements Condition, java.io.Serializable {
        private static final long serialVersionUID = 1173984872572414699L;
        private transient AbstractQueuedSynchronizerNero.Node firstWaiter;
        private transient AbstractQueuedSynchronizerNero.Node lastWaiter;

        /**
         * Creates a new {@code ConditionObject} instance.
         */
        public ConditionObject() { }

        private AbstractQueuedSynchronizerNero.Node addConditionWaiter() {
            AbstractQueuedSynchronizerNero.Node t = lastWaiter;
            if (t != null && t.waitStatus != AbstractQueuedSynchronizerNero.Node.CONDITION) {
                unlinkCancelledWaiters();
                t = lastWaiter;
            }
            AbstractQueuedSynchronizerNero.Node node = new AbstractQueuedSynchronizerNero.Node(Thread.currentThread(), AbstractQueuedSynchronizerNero.Node.CONDITION);
            if (t == null)
                firstWaiter = node;
            else
                t.nextWaiter = node;
            lastWaiter = node;
            return node;
        }


        private void doSignal(AbstractQueuedSynchronizerNero.Node first) {
            do {
                if ( (firstWaiter = first.nextWaiter) == null)
                    lastWaiter = null;
                first.nextWaiter = null;
            } while (!transferForSignal(first) &&
                    (first = firstWaiter) != null);
        }


        private void doSignalAll(AbstractQueuedSynchronizerNero.Node first) {
            lastWaiter = firstWaiter = null;
            do {
                AbstractQueuedSynchronizerNero.Node next = first.nextWaiter;
                first.nextWaiter = null;
                transferForSignal(first);
                first = next;
            } while (first != null);
        }

        private void unlinkCancelledWaiters() {
            AbstractQueuedSynchronizerNero.Node t = firstWaiter;
            AbstractQueuedSynchronizerNero.Node trail = null;
            while (t != null) {
                AbstractQueuedSynchronizerNero.Node next = t.nextWaiter;
                if (t.waitStatus != AbstractQueuedSynchronizerNero.Node.CONDITION) {
                    t.nextWaiter = null;
                    if (trail == null)
                        firstWaiter = next;
                    else
                        trail.nextWaiter = next;
                    if (next == null)
                        lastWaiter = trail;
                }
                else
                    trail = t;
                t = next;
            }
        }


        public final void signal() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            AbstractQueuedSynchronizerNero.Node first = firstWaiter;
            if (first != null)
                doSignal(first);
        }


        public final void signalAll() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            AbstractQueuedSynchronizerNero.Node first = firstWaiter;
            if (first != null)
                doSignalAll(first);
        }


        public final void awaitUninterruptibly() {
            AbstractQueuedSynchronizerNero.Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean interrupted = false;
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                if (Thread.interrupted())
                    interrupted = true;
            }
            if (acquireQueued(node, savedState) || interrupted)
                selfInterrupt();
        }


        private static final int REINTERRUPT =  1;
        private static final int THROW_IE    = -1;

        private int checkInterruptWhileWaiting(AbstractQueuedSynchronizerNero.Node node) {
            return Thread.interrupted() ?
                    (transferAfterCancelledWait(node) ? THROW_IE : REINTERRUPT) :
                    0;
        }


        private void reportInterruptAfterWait(int interruptMode)
                throws InterruptedException {
            if (interruptMode == THROW_IE)
                throw new InterruptedException();
            else if (interruptMode == REINTERRUPT)
                selfInterrupt();
        }

        public final void await() throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            AbstractQueuedSynchronizerNero.Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null) // clean up if cancelled
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
        }

        public final long awaitNanos(long nanosTimeout)
                throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            AbstractQueuedSynchronizerNero.Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            final long deadline = System.nanoTime() + nanosTimeout;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    transferAfterCancelledWait(node);
                    break;
                }
                if (nanosTimeout >= spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                nanosTimeout = deadline - System.nanoTime();
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return deadline - System.nanoTime();
        }


        public final boolean awaitUntil(Date deadline)
                throws InterruptedException {
            long abstime = deadline.getTime();
            if (Thread.interrupted())
                throw new InterruptedException();
            AbstractQueuedSynchronizerNero.Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (System.currentTimeMillis() > abstime) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                LockSupport.parkUntil(this, abstime);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }


        public final boolean await(long time, TimeUnit unit)
                throws InterruptedException {
            long nanosTimeout = unit.toNanos(time);
            if (Thread.interrupted())
                throw new InterruptedException();
            AbstractQueuedSynchronizerNero.Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            final long deadline = System.nanoTime() + nanosTimeout;
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                if (nanosTimeout >= spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                nanosTimeout = deadline - System.nanoTime();
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }


        final boolean isOwnedBy(AbstractQueuedSynchronizerNero sync) {
            return sync == AbstractQueuedSynchronizerNero.this;
        }


        protected final boolean hasWaiters() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            for (AbstractQueuedSynchronizerNero.Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == AbstractQueuedSynchronizerNero.Node.CONDITION)
                    return true;
            }
            return false;
        }



        protected final int getWaitQueueLength() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            int n = 0;
            for (AbstractQueuedSynchronizerNero.Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == AbstractQueuedSynchronizerNero.Node.CONDITION)
                    ++n;
            }
            return n;
        }


        protected final Collection<Thread> getWaitingThreads() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            ArrayList<Thread> list = new ArrayList<Thread>();
            for (AbstractQueuedSynchronizerNero.Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == AbstractQueuedSynchronizerNero.Node.CONDITION) {
                    Thread t = w.thread;
                    if (t != null)
                        list.add(t);
                }
            }
            return list;
        }
    }



    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizerNero.Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizerNero.Node.class.getDeclaredField("next"));

        } catch (Exception ex) { throw new Error(ex); }
    }

    private final boolean compareAndSetHead(AbstractQueuedSynchronizerNero.Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    private final boolean compareAndSetTail(AbstractQueuedSynchronizerNero.Node expect, AbstractQueuedSynchronizerNero.Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    private static final boolean compareAndSetWaitStatus(AbstractQueuedSynchronizerNero.Node node,
                                                         int expect,
                                                         int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset,
                expect, update);
    }

    private static final boolean compareAndSetNext(AbstractQueuedSynchronizerNero.Node node,
                                                   AbstractQueuedSynchronizerNero.Node expect,
                                                   AbstractQueuedSynchronizerNero.Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }
}
