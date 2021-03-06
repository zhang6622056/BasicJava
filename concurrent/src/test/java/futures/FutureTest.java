package futures;

import org.junit.Test;

import java.util.concurrent.*;

public class FutureTest {


    @Test
    public void sinpleFutureTask() throws Exception {

        Callable callable = new Callable<Integer>(){
            @Override
            public Integer call() throws Exception {
                return Integer.valueOf(1);
            }
        };


        Integer a = (Integer) callable.call();
        System.out.println(a);

//        FutureTask futureTask = new Future<>(callable);
    }
















    @Test
    public void testFutureTask(){
        FutureTask<String> futureTask = new FutureTask<String>(initCallAble());
        try {
            new Thread(futureTask).start();


            System.out.println("线程继续执行了。。。");
            System.out.println(futureTask.get(3l, TimeUnit.SECONDS));
            System.out.println("DONE:"+futureTask.isDone());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }



    private Callable<String> initCallAble(){
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("====================");
                Thread.currentThread().sleep(2000l);
                return "Callable执行完毕！！";
            }
        };
    }
}
