package chapter14;

import java.math.BigDecimal;

public class Chapter14 {


    public static void main(String[] args) {

       final Account a = new Account();
       final Account b = new Account();



        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    a.addBalance(b, BigDecimal.valueOf(100));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



            }
        });


        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    b.addBalance(a,BigDecimal.valueOf(100));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });




        t1.start();
        t2.start();




















    }







}
