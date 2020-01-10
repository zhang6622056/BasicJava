package another.lockconditionblockingqueue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;





/***
 *
 * 实现一个阻塞队列。
 * @author Nero
 * @date 2020-01-09
 * *@param: null
 * @return 
 */
public class BlockingQueue<T> {


    private int size;
    private final Lock lock;
    private final Condition pushCondition;
    private final Condition popCondition;

    private final List<T> list;




    public BlockingQueue(int size) {
        if (size == 0){throw new RuntimeException("size不可为0");}
        this.size = size;
        lock = new ReentrantLock();

        pushCondition = lock.newCondition();
        popCondition = lock.newCondition();
        list = new ArrayList<>();
    }


    //- 增加一个对象至队列中
    public void push(T obj){
        lock.lock();
        try{
            //- 队列已满，不允许入队,阻塞至条件变量队列
            if (list.size() == size){
                pushCondition.await();
            }

            System.out.println("push   threadId:"+Thread.currentThread().getId());
            list.add(obj);

            //- 随机通知一个线程，当有元素入队，则通知可出队
            popCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }






    //- 弹出一个元素
    public T pop(){
        T obj = null;
        lock.lock();
        try{
            //- 如果队列为空，则阻塞等待
            if (list.size() == 0){
                popCondition.await();
            }

            int index = list.size()-1;
            obj = list.get(index);
            list.remove(list.size()-1);

            //- 当有元素出队，此时队列不满，可以通知入队队列的条件队列线程入队
            pushCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }


        return obj;
    }


    public static void main(String[] args) throws IOException {
        BlockingQueue<String> blockingQueue = new BlockingQueue(2);



        List<Thread> listsPush = new ArrayList<>();
        List<Thread> listPop = new ArrayList<>();

        for (int i = 0 ; i < 50 ; i++){
            listsPush.add(new Thread(new Runnable() {
                @Override
                public void run() {

                    blockingQueue.push(String.valueOf("2222"));
                }
            }));
        }



        for (int i = 0 ; i < 50 ; i++){
            listPop.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("threadId:"+Thread.currentThread().getId() + "pop:"+blockingQueue.pop());
                }
            }));
        }




        for (int i = 0 ; i < 50; i++){
            listsPush.get(i).start();
           // listPop.get(i).start();
        }



        System.in.read();



    }






}
