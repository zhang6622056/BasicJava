package tests;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class Decimals {


    @Test
    public void test(){
        BigDecimal c = new BigDecimal(1000);
        BigDecimal a = c.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
        System.out.println(a);
    }


}
