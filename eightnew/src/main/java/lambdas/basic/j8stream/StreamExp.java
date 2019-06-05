package lambdas.basic.j8stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StreamExp {



    public static List<Integer> generateList(int size){
        List<Integer> list = new ArrayList<>();

        Random random = new Random();
        for (int i = 0 ; i < size ;i++){
            list.add(random.nextInt(100));
        }
        return list;
    }


    /***
     * 比较正向排列
     * @param c1
     * @param c2
     * @return
     */
    public static int compareAsc(int c1,int c2){
        if (c1 == c2) return 0;
        return c1 > c2 ? 1 : -1;
    }

    /***
     * 比较逆向排列
     * @param c1
     * @param c2
     * @return
     */
    public static int compareDesc(int c1,int c2){
        if (c1 == c2) return 0;
        return c1 > c2 ? -1 : 1;
    }

    /***
     * 过滤器
     * @return
     */
    public static boolean filterFun(int obj){
        return (obj < 100);
    }


    /***
     * 乘以2函数
     * @param obj
     * @return
     */
    public static int mapMultiplyTwoFun(int obj){
        return obj * 2;
    }







    public static Boolean judge(Integer item){
        System.out.println(item);
        if (item < 10) return true;
        return false;
    }



    public static void main(String[] args) {
        StreamExp streamExp = new StreamExp();

        List<Integer> list = streamExp.generateList(10);




        List<Integer> res = list.stream().filter(i -> i < 10).collect(Collectors.toList());






//
//
//        Map<Integer,Boolean> res = list.parallelStream().collect(
//                Collectors.toMap(item -> item,StreamExp::judge)
//        );

//        System.out.println(res.size());
    }






}
