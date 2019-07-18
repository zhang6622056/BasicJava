package chapter26;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;



/**
 *
 * 从0+到100
 * @author Nero
 * @date 2019-07-18
 * *@param: null
 * @return
 */
public class ForkJoinExp {

    public static void main(String[] args) {
        int[] arr = new int[100];
        for (int i = 0 ; i < 100 ; i++){
            arr[i] = i;
        }

        //fork/join线程池
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        //创建分治任务
        Fibonacci fibonacci = new Fibonacci(arr,0,arr.length);
        //执行
        Integer result = forkJoinPool.invoke(fibonacci);




        System.out.println(result);
    }

    /**
     *
     * 斐波那契
     * @author Nero
     * @date 2019-07-18
     * *@param: null
     * @return
     */
    static class Fibonacci extends RecursiveTask<Integer>{
        private static final int THRESHOLD = 20;


        private int arr[];
        private int start;
        private int end;

        public Fibonacci(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }


        private Integer subtotal(){
            Integer sum = 0;
            for (int i = start ; i < end ; i++){
                sum += arr[i];
            }

            System.out.println(Thread.currentThread().getName() + ": ∑(" + start + "~" + end + ")=" + sum);
            return sum;
        }




        @Override
        protected Integer compute() {
            if ((end - start) <= THRESHOLD){        //真正的任务逻辑
                return subtotal();
            }else{                                  //二分执行拆分合并
                int middle = (start+end) / 2;
                Fibonacci left = new Fibonacci(arr,start,middle);
                Fibonacci right = new Fibonacci(arr,middle,end);
                left.fork();
                right.fork();

                return left.join()+right.join();
            }
        }
    }
}
