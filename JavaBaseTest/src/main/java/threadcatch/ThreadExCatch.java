package threadcatch;

import java.io.IOException;

public class ThreadExCatch {

    public static void main(String[] args) throws IOException {
        new Thread(() ->{
            int a = 1/0;
            System.out.println("aaaa");
        }).start();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(e.getMessage());
            }
        });
        System.in.read();
    }




}
