package testfromalibaba.designforsort;

import testfromalibaba.designforsort.compare.CompareBase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CompareUtil {
    public static void sort(List<MobileNumber> mobiles, CompareBase compareChain){
        Collections.sort(mobiles,compareChain);
    }
}
