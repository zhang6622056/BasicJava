package testfromalibaba.designforsort.compare;

import java.lang.reflect.Field;
import java.util.Date;


/***
 * @Description  创建时间比较器
 * @Author zhanglei
 * @Date 2020/12/8 1:39 上午
 **/
public class CompareBaseCreateTime<MobileNumber> implements CompareBase<MobileNumber> {
    private CompareBase<MobileNumber> next;

    @Override
    public String getFieldName() {
        return "createTime";
    }

    @Override
    public void setNext(CompareBase compareBase) {
        this.next = compareBase;
    }

    @Override
    public CompareBase getNext() {
        return next;
    }

    @Override
    public int compare(MobileNumber o1, MobileNumber o2) {
        Field f1 = null;
        try {
            f1 = o1.getClass().getDeclaredField(getFieldName());
            Field f2 = o2.getClass().getDeclaredField(getFieldName());
            f1.setAccessible(true);
            f2.setAccessible(true);
            Object d1o = f1.get(o1);
            Object d2o = f1.get(o1);
            if (d1o == d2o){
                return null == getNext() ? 0 : next.compare(o1,o2);
            }

            if (d1o == null){return -1;}
            if (d2o == null){return 1;}
            Date d1 = (Date)d1o;
            Date d2 = (Date)d2o;
            return d1.before(d2) ? 1 : -1;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
