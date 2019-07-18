package chapter26;

import org.junit.Test;

public class Chapter26Test {

    @Test
    public void test(){
        int sum = 0;
        for (int i = 0 ; i < 100 ; i++){
            sum += i;
        }
        System.out.println(sum);
    }



}
