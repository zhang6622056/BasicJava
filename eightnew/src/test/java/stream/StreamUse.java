package stream;

import lambdas.basic.j8stream.StreamExp;
import lambdas.basic.randoms.RandomBean;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class StreamUse {
    @Test
    public void findFirst(){
        List<Integer> list = StreamExp.generateList(10);

        List<Integer> forRemove = list.stream().filter(obj -> obj < 50).collect(Collectors.toList());


        System.out.println("for remove size:"+forRemove.size());
        for (Integer a : forRemove){
            list.remove(a);
        }

        System.out.println(list.size());



        for (int i = 0 ; i < forRemove.size() ; i++){
            list.remove(i);
            i--;
        }






//        Optional<Integer> a = list.stream().unordered()
//                .sorted(StreamExp::compareDesc)
//                .filter(StreamExp::filterFun)
//                .map(StreamExp::mapMultiplyTwoFun)
//                .findFirst();
//
//        System.out.println(a.get());
    }








    @Test
    public void test(){
        List<Integer> list = StreamExp.generateList(10);

        List<Integer> a = list.stream().unordered()
                .sorted(StreamExp::compareDesc)
                .filter(StreamExp::filterFun)
                .map(StreamExp::mapMultiplyTwoFun)
                .collect(Collectors.toList());




        a.forEach( obj -> System.out.println(obj));
    }











}
