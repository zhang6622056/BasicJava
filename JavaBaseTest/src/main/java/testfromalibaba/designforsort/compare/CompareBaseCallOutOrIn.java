package testfromalibaba.designforsort.compare;

import java.lang.reflect.Field;


/***
 * @Description  呼出呼入次数比较器
 * @Author zhanglei
 * @Date 2020/12/8 1:39 上午
 **/
public class CompareBaseCallOutOrIn<MobileNumber> implements CompareBase<MobileNumber>{

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public String getFieldName() {
        return "callOutTimes";
    }


    @Override
    public int compare(MobileNumber o1, MobileNumber o2) {
        try {
            Field f1 = o1.getClass().getDeclaredField(getFieldName());
            Field f2 = o2.getClass().getDeclaredField(getFieldName());
            f1.setAccessible(true);
            f2.setAccessible(true);
            Object times1 = f1.get(o1);
            Object times2 = f2.get(o2);

            if (times1 == times2){return 0;}
            if (times1 == null){return -1;}
            if (times2 == null){return 1;}

            Integer int1 = (Integer) times1;
            Integer int2 = (Integer) times2;
            return int1 > int2 ? 1 : -1;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
