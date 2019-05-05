package stream;

import lambdas.basic.randoms.RandomBean;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class StreamUse {

    @Test
    public void test(){
        List a = new ArrayList();
        a.add(2);
        a.add(1);
        a.add(3);
        a.add(4);
        a.add(5);
        Spliterator spliterator = Spliterators.spliterator(a,0);
        //System.out.println(spliterator.getComparator());
        System.out.println(spliterator.characteristics());

        a.stream().distinct();

    }






}
