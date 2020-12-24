package testfromalibaba.designforsort.compare;

import java.util.Comparator;

public interface CompareBase<MobileNumber> extends Comparator<MobileNumber> {
    //- 比较器优先级
    int getPriority();
    //- 获取属性名称，反射用到
    String getFieldName();
}
