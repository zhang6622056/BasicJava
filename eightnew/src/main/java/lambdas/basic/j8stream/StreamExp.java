package lambdas.basic.j8stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StreamExp {



    public List<Integer> generateList(int size){
        List<Integer> list = new ArrayList<>();

        Random random = new Random();
        for (int i = 0 ; i < size ;i++){
            list.add(random.nextInt(100));
        }
        return list;
    }




    public static Boolean judge(Integer item){
        System.out.println(item);
        if (item < 10) return true;
        return false;
    }



    public static void main(String[] args) {
        StreamExp streamExp = new StreamExp();

        List<Integer> list = streamExp.generateList(10);

        List<Integer> res = list.stream().filter(i -> i < 10).limit(3).distinct().collect(Collectors.toList());
        for (Integer a : res){
            System.out.println(a);
        }



//
//
//        Map<Integer,Boolean> res = list.parallelStream().collect(
//                Collectors.toMap(item -> item,StreamExp::judge)
//        );

//        System.out.println(res.size());
    }






}
