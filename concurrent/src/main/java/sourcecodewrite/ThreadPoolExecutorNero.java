//package sourcecodewrite;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.concurrent.*;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.locks.AbstractQueuedSynchronizer;
//import java.util.concurrent.locks.ReentrantLock;
//
//public class ThreadPoolExecutorNero extends AbstractExecutorService {
//
//    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
//    private static final int COUNT_BITS = Integer.SIZE - 3;
//    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;
//
//    //- 线程池状态
//    private static final int RUNNING    = -1 << COUNT_BITS;
//    private static final int SHUTDOWN   =  0 << COUNT_BITS;
//    private static final int STOP       =  1 << COUNT_BITS;
//    private static final int TIDYING    =  2 << COUNT_BITS;
//    private static final int TERMINATED =  3 << COUNT_BITS;
//
//
//
//
//
//
//    //- 线程池的当前运行状态
//    private static int runStateOf(int c)     { return c & ~CAPACITY; }
//    //- 获取线程池的线程数量
//    private static int workerCountOf(int c)  { return c & CAPACITY; }
//    //- TODO
//    private static int ctlOf(int rs, int wc) { return rs | wc; }
//
//
//
//
//
//    private static boolean runStateLessThan(int c, int s) {
//        return c < s;
//    }
//
//    private static boolean runStateAtLeast(int c, int s) {
//        return c >= s;
//    }
//    //- 是否运行态
//    private static boolean isRunning(int c) { return c < SHUTDOWN; }
//
//    //TODO-ZL
//    private static final boolean ONLY_ONE = true;
//
//
//
//
//    //- 线程池相关参数
//    private volatile int corePoolSize;
//    private volatile int maximumPoolSize;
//    private volatile long keepAliveTime;
//    private volatile RejectedExecutionHandler handler;
//    private volatile ThreadFactory threadFactory;
//    private final BlockingQueue<Runnable> workQueue;
//
//    //- 线程池的worker容器
//    private final HashSet<ThreadPoolExecutorNero.Worker> workers = new HashSet<ThreadPoolExecutorNero.Worker>();
//    //- 锁对象，涉及操作：1- 增加worker操作
//    private final ReentrantLock mainLock = new ReentrantLock();
//    //-TODO-ZL  不知道干什么用的。
//    private int largestPoolSize;
//
//
//
//    public ThreadPoolExecutorNero(int corePoolSize,
//                                  int maximumPoolSize,
//                                  long keepAliveTime,
//                                  TimeUnit unit,
//                                  BlockingQueue<Runnable> workQueue,
//                                  ThreadFactory threadFactory,
//                                  RejectedExecutionHandler handler) {
//        if (corePoolSize < 0 ||
//                maximumPoolSize <= 0 ||
//                maximumPoolSize < corePoolSize ||
//                keepAliveTime < 0)
//            throw new IllegalArgumentException();
//        if (workQueue == null || threadFactory == null || handler == null)
//            throw new NullPointerException();
////        this.acc = System.getSecurityManager() == null ?
////                null :
////                AccessController.getContext();
//        this.corePoolSize = corePoolSize;
//        this.maximumPoolSize = maximumPoolSize;
//        this.workQueue = workQueue;
//        this.keepAliveTime = unit.toNanos(keepAliveTime);
//        this.threadFactory = threadFactory;
//        this.handler = handler;
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    @Override
//    public void shutdown() {
//
//    }
//
//    @Override
//    public List<Runnable> shutdownNow() {
//        return null;
//    }
//
//    @Override
//    public boolean isShutdown() {
//        return false;
//    }
//
//    @Override
//    public boolean isTerminated() {
//        return false;
//    }
//
//    @Override
//    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
//        return false;
//    }
//
//
//
//    //- 删除队列中的任务
//    public boolean remove(Runnable task) {
//        boolean removed = workQueue.remove(task);
//        tryTerminate();
//        return removed;
//    }
//
//
//    @Override
//    public void execute(Runnable command) {
//        if (command == null) {throw new NullPointerException();}
//
//
//        //- 如果线程数小于核心线程数，则增加worker线程委派。
//        int c = ctl.get();
//        if (workerCountOf(c) < corePoolSize){
//            if (addWorker(command, true)){ return;}
//        }
//
//
//        //- 核心线程数已满，尝试放入队列中
//        c = ctl.get();
//        if (isRunning(c) && workQueue.offer(command)){
//            int recheck = ctl.get();
//            if (!isRunning(recheck) && remove(command)){
//                reject(command);
//            }else if (workerCountOf(recheck) == 0){
//                addWorker(command, true);
//                return;
//            }
//        }else if (!addWorker(command, false)){  //- 核心线程数已满，队列已满。尝试增加max-core的线程
//            reject(command);
//        }
//    }
//
//
//
//
//    //- param2 不理会，警醒个人
//    final void reject(Runnable command) {
//       // handler.rejectedExecution(command, this);
//    }
//
//    //- 通过自旋更新ctl值
//    private boolean compareAndIncrementWorkerCount(int expect) {
//        return ctl.compareAndSet(expect, expect + 1);
//    }
//
//
//    /***
//     *
//     * 增加worker线程
//     * @author Nero
//     * @date 2019-12-02
//     * @param: firstTask
//     * @param: core
//     * @return boolean
//     */
//    private boolean addWorker(Runnable firstTask, boolean core) {
//        //- 不断尝试自旋，更新线程池thread数量
//        retry:
//        for (;;) {
//            int c = ctl.get();
//            int rs = runStateOf(c);
//
//            //- 检查运行态，非运行态直接返回
//            if (rs >= SHUTDOWN &&
//                    ! (rs == SHUTDOWN &&
//                            firstTask == null &&
//                            ! workQueue.isEmpty()))
//                return false;
//
//            //- 检查线程数量
//            for (;;) {
//                int wc = workerCountOf(c);
//                if (wc >= CAPACITY || wc >= (core ? corePoolSize : maximumPoolSize)){return false;}
//
//                //- 防止并发更新线程数，采用cas自旋
//                if (compareAndIncrementWorkerCount(c)){break retry;}
//
//                c = ctl.get();
//                if (runStateOf(c) != rs){continue retry;}
//
//            }
//        }
//
//
//        //- 增加worker线程到worker容器
//        boolean workerStarted = false;
//        boolean workerAdded = false;
//        ThreadPoolExecutorNero.Worker w = null;
//        try {
//            w = new ThreadPoolExecutorNero.Worker(firstTask);
//            final Thread t = w.thread;
//            if (t != null) {
//
//                //- 加锁操作增加workders线程集合.
//                final ReentrantLock mainLock = this.mainLock;
//                mainLock.lock();
//                try {
//                    int rs = runStateOf(ctl.get());
//                    if (rs < SHUTDOWN || (rs == SHUTDOWN && firstTask == null)) {
//                        if (t.isAlive()) {throw new IllegalThreadStateException();}
//                        workers.add(w);
//                        int s = workers.size();
//                        if (s > largestPoolSize){largestPoolSize = s;}
//
//                        workerAdded = true;
//                    }
//                } finally {
//                    mainLock.unlock();
//                }
//                if (workerAdded) {
//                    t.start();
//                    workerStarted = true;
//                }
//            }
//        } finally {
//            //- 增加线程失败，回滚ctl
//            if (! workerStarted){addWorkerFailed(w);}
//        }
//        return workerStarted;
//    }
//
//
//
//
//    /***
//     *
//     * 增加worker失败，回滚增加操作
//     * - 回滚workers集合
//     * - 回滚ctl值
//     * @author Nero
//     * @date 2019-12-02
//     * *@param: w
//     * @return void
//     */
//    private void addWorkerFailed(ThreadPoolExecutorNero.Worker w) {
//        final ReentrantLock mainLock = this.mainLock;
//        mainLock.lock();
//        try {
//            if (w != null){
//                workers.remove(w);
//                decrementWorkerCount();
//                tryTerminate();
//            }
//        } finally {
//            mainLock.unlock();
//        }
//    }
//
//
//
//
//    //- cas更新ctl值
//    private boolean compareAndDecrementWorkerCount(int expect) {
//        return ctl.compareAndSet(expect, expect - 1);
//    }
//
//    //- 采用do{} while循环一直到cas更新成功
//    private void decrementWorkerCount() {
//        do {} while (!compareAndDecrementWorkerCount(ctl.get()));
//    }
//
//
//
//
//
//    /***
//     * 尝试终止线程池
//     * - 运行态，TIDYING，SHUTDOWN，或者任务队列不为空都直接return
//     * - 设置线程的中断标示
//     * - 设置线程池状态为TIDYING,调用terminated方法，设置线程池终止态
//     * @author Nero
//     * @date 2019-12-02
//     * *@param:
//     * @return void
//     */
//    final void tryTerminate() {
//        for (;;) {
//            int c = ctl.get();
//
//            //- 运行态，TIDYING，SHUTDOWN，或者任务队列不为空都直接return
//            if (isRunning(c) ||
//                    runStateAtLeast(c, TIDYING) ||
//                    (runStateOf(c) == SHUTDOWN && ! workQueue.isEmpty())){
//                return;
//            }
//
//            //- 设置线程的中断标示
//            if (workerCountOf(c) != 0) {
//                interruptIdleWorkers(ONLY_ONE);
//                return;
//            }
//
//
//            //- 设置线程池状态为TIDYING,
//            //- 调用terminated方法
//            //- 设置线程池终止态
//            final ReentrantLock mainLock = this.mainLock;
//            mainLock.lock();
//            try {
//                if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
//                    try {
////                        terminated();
//                    } finally {
//                        ctl.set(ctlOf(TERMINATED, 0));
////                        termination.signalAll();
//                    }
//                    return;
//                }
//            } finally {
//                mainLock.unlock();
//            }
//        }
//    }
//
//
//
//
//
//    //- TODO-ZL UNKNOWN 设置线程池中的线程中断标示
//    private void interruptIdleWorkers(boolean onlyOne) {
//        final ReentrantLock mainLock = this.mainLock;
//        mainLock.lock();
//        try {
//            for (ThreadPoolExecutorNero.Worker w : workers) {
//                Thread t = w.thread;
//
//                //- 如果线程没有设置中断标示,则给线程设置中断标示。
//                //- 每个线程都有自己的锁，继承aqs
//                if (!t.isInterrupted() && w.tryLock()) {
//                    try {
//                        t.interrupt();
//                    } catch (SecurityException ignore) {
//                    } finally {
//                        w.unlock();
//                    }
//                }
//                if (onlyOne){break;}
//            }
//        } finally {
//            mainLock.unlock();
//        }
//    }
//
//
//
//
//
//    public void setThreadFactory(ThreadFactory threadFactory) {
//        if (threadFactory == null) { throw new NullPointerException(); }
//        this.threadFactory = threadFactory;
//    }
//    public ThreadFactory getThreadFactory() {
//        return threadFactory;
//    }
//
//
//
//
//
//
//
//
//
//    final void runWorker(ThreadPoolExecutorNero.Worker w) {
//        Thread wt = Thread.currentThread();
//        Runnable task = w.firstTask;
//        w.firstTask = null;
//        w.unlock();
//        boolean completedAbruptly = true;
//        try {
//            while (task != null || (task = getTask()) != null) {
//                w.lock();
//                if ((runStateAtLeast(ctl.get(), STOP) ||
//                        (Thread.interrupted() &&
//                                runStateAtLeast(ctl.get(), STOP))) &&
//                        !wt.isInterrupted())
//                    wt.interrupt();
//                try {
//                    beforeExecute(wt, task);
//                    Throwable thrown = null;
//                    try {
//                        task.run();
//                    } catch (RuntimeException x) {
//                        thrown = x; throw x;
//                    } catch (Error x) {
//                        thrown = x; throw x;
//                    } catch (Throwable x) {
//                        thrown = x; throw new Error(x);
//                    } finally {
//                        afterExecute(task, thrown);
//                    }
//                } finally {
//                    task = null;
//                    w.completedTasks++;
//                    w.unlock();
//                }
//            }
//            completedAbruptly = false;
//        } finally {
//            processWorkerExit(w, completedAbruptly);
//        }
//    }
//
//
//    private final class Worker extends AbstractQueuedSynchronizer implements Runnable{
//        //- 本worker封装的thread线程对象，每次构造新增一个
//        final Thread thread;
//        //- 提交的任务
//        Runnable firstTask;
//        volatile long completedTasks;
//
//
//        Worker(Runnable firstTask) {
//            //TODO setState -1 AQS知识
//            setState(-1);
//            this.firstTask = firstTask;
//            this.thread = getThreadFactory().newThread(this);
//        }
//
//
//        @Override
//        public void run() {
//            runWorker(this);
//        }
//
//
//        //TODO-ZL UNKNOWN
//        public void lock()        { acquire(1); }
//        public boolean tryLock()  { return tryAcquire(1); }
//        public void unlock()      { release(1); }
//        public boolean isLocked() { return isHeldExclusively(); }
//
//
//
//
//        protected boolean isHeldExclusively() {
//            return getState() != 0;
//        }
//
//
//
//        /***
//         * 尝试获取锁
//         * - 记录state为1
//         * - 缓存持有锁的线程对象
//         * @author Nero
//         * @date 2019-12-04
//         * *@param: unused
//         * @return boolean
//         */
//        protected boolean tryAcquire(int unused) {
//            //- 尝试获取锁
//            if (compareAndSetState(0, 1)) {
//                //- 缓存当前持有锁的线程
//                setExclusiveOwnerThread(Thread.currentThread());
//                return true;
//            }
//            return false;
//        }
//
//
//
//        /***
//         *
//         * 尝试释放锁
//         * - 清空持有锁的线程对象
//         * - 设置状态为0
//         * @author Nero
//         * @date 2019-12-04
//         * *@param: unused
//         * @return boolean
//         */
//        protected boolean tryRelease(int unused) {
//            setExclusiveOwnerThread(null);
//            setState(0);
//            return true;
//        }
//
//
//
//        void interruptIfStarted() {
//            Thread t;
//            if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
//                try {
//                    t.interrupt();
//                } catch (SecurityException ignore) {
//                }
//            }
//        }
//    }
//
//
//
//}
