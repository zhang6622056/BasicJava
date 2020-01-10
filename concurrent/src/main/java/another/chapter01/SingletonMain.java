package another.chapter01;

import java.util.concurrent.CountDownLatch;

public class SingletonMain {


    private static final CountDownLatch countDownLatch = new CountDownLatch(10);





    public static void main(String[] args) throws InterruptedException {
        for (int i = 0 ; i < 100 ; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    countDownLatch.countDown();
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Singleton.getInstance().getUsername().toString());
                }
            }).start();
        }


        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }






}
