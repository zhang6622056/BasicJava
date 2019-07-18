package chapter25;

import java.util.Random;
import java.util.concurrent.*;



/**
 *
 * future.get 先执行完的先执行后续操作。普通实现
 * @author Nero
 * @date 2019-07-18
 * *@param: null
 * @return
 */
public class Chapter25WithOutCompletionService {



    private static final Random random = new Random();
    private static final ExecutorService searchExecutorPool = Executors.newFixedThreadPool(10);
    private static final ExecutorService saveExecutorPool = Executors.newFixedThreadPool(10);


    /**
     *
     * 询价业务
     * @author Nero
     * @date 2019-07-18
     * @return void
     */
    private Integer getPrice(int platform){
        if (platform == 1){
            try {
                Thread.sleep(10000);
                return 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (platform == 2){
            try {
                Thread.sleep(2000);
                return 2;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (platform == 3){
            return 3;
        }

        return random.nextInt();
    }

    /*
     *
     * 本地保存询问好的价格
     * @author Nero
     * @date 2019-07-18
     * *@param: a
     * @return void
     */
    private void save(Integer a){
        System.out.println(a);
    }



    /**
     *
     * 询价错误阻塞版本
     * @author Nero
     * @date 2019-07-18
     * @return void
     */
    public void askPriceOne(){

        //异步询价1
        Future<Integer> f1 = searchExecutorPool.submit(()->getPrice(1));
        Future<Integer> f2 = searchExecutorPool.submit(()->getPrice(2));
        Future<Integer> f3 = searchExecutorPool.submit(()->getPrice(3));



        //要注意，这里的询价可能会f1特别慢，但是f3特别快
        try {
            Integer a1 = f1.get();
            saveExecutorPool.execute(()->save(a1));
            Integer a2 = f2.get();
            saveExecutorPool.execute(()->save(a2));
            Integer a3 = f3.get();
            saveExecutorPool.execute(()->save(a3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }



    /**
     *
     * 询价修正版本
     * 在子线程中阻塞，并且将结果添加到阻塞队列，在其他线程中消费
     *
     * @author Nero
     * @date 2019-07-18
     * *@param:
     * @return void
     */
    public void askPriceTwo(){
        BlockingQueue<Integer> bq = new LinkedBlockingQueue<>();

        //异步询价1
        Future<Integer> f1 = searchExecutorPool.submit(()->getPrice(1));
        Future<Integer> f2 = searchExecutorPool.submit(()->getPrice(2));
        Future<Integer> f3 = searchExecutorPool.submit(()->getPrice(3));


        saveExecutorPool.execute(() -> {
            try {
                bq.put(f1.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        saveExecutorPool.execute(() -> {
            try {
                bq.put(f2.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        saveExecutorPool.execute(() -> {
            try {
                bq.put(f3.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });



        while(true){
            if (!bq.isEmpty()){
                try {
                    Integer res = bq.take();
                    saveExecutorPool.execute(()-> save(res));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    /*
     * @author Nero
     * @date 2019-07-18
     * *@param: args
     * @return void
     */
    public static void main(String[] args) {
        Chapter25WithOutCompletionService chapter25WithOutCompletionService = new Chapter25WithOutCompletionService();
        chapter25WithOutCompletionService.askPriceTwo();
    }
}
