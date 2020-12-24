package testfromalibaba.designforsort.compare;

import java.util.Comparator;

public interface CompareBase<MobileNumber> extends Comparator<MobileNumber> {
    //- 获取属性名称，反射用到
    String getFieldName();
    //- 设置下一个比较器
    void setNext(CompareBase compareBase);
    //- 获取下一个比较器
    CompareBase getNext();
}
