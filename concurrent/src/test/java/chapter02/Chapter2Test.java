package chapter02;

import org.junit.Test;

public class Chapter2Test {

    @Test
    public void test(){
        Chapter2 chapter2 = new Chapter2();
        for (int i = 0 ; i < 1000 ; i++){
            new Thread(chapter2,String.valueOf(i)).start();
        }
        System.out.println(chapter2.x);
    }
}
