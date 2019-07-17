package chapter23;

import java.util.concurrent.*;



/***
 * submit 方法初体验，3种构造,最后一种忒难玩儿。。不玩了。。
 *
 * @author Nero
 * @date 2019-07-17
 * *@param: null
 * @return 
 */
public class Chapter23Init {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);


        //-无结果的返回
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getId()+"runed..");
            }
        });

        //-有结果的返回
        Callable callableWithRes = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return String.valueOf("有结果的返回...");
            }
        };

        Future<String> future = executorService.submit(callableWithRes);
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
