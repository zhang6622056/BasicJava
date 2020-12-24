package testfromalibaba.designforsort;

import testfromalibaba.designforsort.compare.CompareBase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CompareUtil {


    public static void sort(List<MobileNumber> mobiles, List<CompareBase> compareRule){
        //- 将排序规则，按照优先级排序
        sortComparePriority(compareRule);

        //- 应用按照排序好的优先级比较器，排序号码
        for (CompareBase compareBase : compareRule){
            Collections.sort(mobiles,compareBase);
        }
    }

    /***
     * @Description  对比较器的优先级排序
     * @Author zhanglei
     * @Date 2020/12/8 1:42 上午
     * @param compares :
     * @return : void
     **/
    private static void sortComparePriority(List<CompareBase> compares){
        Collections.sort(compares, new Comparator<CompareBase>() {
            @Override
            public int compare(CompareBase o1, CompareBase o2) {
                if (o1.getPriority() == o2.getPriority()){return 0;}
                return o1.getPriority() > o2.getPriority() ? 1 : -1;
            }
        });
    }





}
