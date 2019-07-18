package chapter25;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/***
 * 类似Dubbo Cluster,获取最快，最优的服务
 * 用CompletionService 实现，查找最快的服务
 * 只要有一个返回，就关闭所有服务
 *
 *
 *
 *
 *
 * @author Nero
 * @date 2019-07-18
 * *@param: null
 * @return
 */
public class TheFastedServerWithCompletionService {


    private static ExecutorService executorService = Executors.newFixedThreadPool(3);



    public static void main(String[] args) {

        List<Future<Integer>> futures = new ArrayList<>();
        CompletionService completionService = new ExecutorCompletionService(executorService);

        //记录所有任务，取消用到
        futures.add(completionService.submit(() -> CompletionServiceExp.getPrice(1)));
        futures.add(completionService.submit(() -> CompletionServiceExp.getPrice(2)));
        futures.add(completionService.submit(() -> CompletionServiceExp.getPrice(3)));



        boolean isOver = false;
        while(true){
            try {
                Future<Integer> future = completionService.take();
                if (isOver) break;

                if (null != future){
                    Integer res = future.get();
                    System.out.println("the fast res is : "+ res);
                    isOver = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println("执行完毕....");


        executorService.shutdown();
    }
}
