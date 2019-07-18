package chapter25;

import java.util.concurrent.*;



/**
 *
 * completionService 使得你无需关注线程执行的内部细节。它批量执行线程任务，执行的快的先入队列。
 * take方法：如果移除一个元素的时候，队列是空的，那么线程将被阻塞
 * poll方法:如果移除一个元素的时候，队列是空的，那么线程将返回null，poll支持超时get
 *
 * @author Nero
 * @date 2019-07-18
 * *@param: null
 * @return 
 */
public class CompletionServiceExp {




    //-创建线程池对象
    private static ExecutorService executorService = Executors.newFixedThreadPool(3);




    /**
     *
     * 询价业务
     * @author Nero
     * @date 2019-07-18
     * @return void
     */
    public static Integer getPrice(int platform){
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

        return 123;
    }




    /***
     *
     * 使用completionService批量执行任务
     * 谨记，completionService的take方法会影响队列，不是get方法，不可多次调用
     * completionService 使得你无需关注线程执行的内部细节。它批量执行线程任务，执行的快的先入队列。
     *
     * @author Nero
     * @date 2019-07-18
     * *@param: args
     * @return void
     */
    public static void main(String[] args) {
        CompletionServiceExp completionServiceExp = new CompletionServiceExp();

        //-批量执行任务对象,异步询价
        CompletionService completionService = new ExecutorCompletionService(executorService);
        completionService.submit(() -> completionServiceExp.getPrice(1));
        completionService.submit(() -> completionServiceExp.getPrice(2));
        completionService.submit(() -> completionServiceExp.getPrice(3));

        while(true){
            try {
                Future<Integer> fure = completionService.take();
                if (null != fure){
                    Integer a = (Integer) fure.get();
                    System.out.println(a);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }
}
