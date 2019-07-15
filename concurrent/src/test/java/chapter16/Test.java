package chapter16;

import chapter15.DubboSourceForSync;



/***
 *
 * 多线程限流器的实现
 * @author Nero
 * @date 2019-07-15
 * @return
 */
public class Test {


    public static void main(String[] args) {
        String a = new String("123");
        final ObjPool objPool = new ObjPool(5,a);


        for (int i = 0 ; i < 50 ; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    objPool.exec();
                }
            }).start();
        }



    }



}
