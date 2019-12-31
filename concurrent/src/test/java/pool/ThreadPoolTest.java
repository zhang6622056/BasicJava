package pool;

import java.util.HashMap;
import java.util.Map;

public class ThreadPoolTest {

    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;


    //- 用于系统查看线程池
    private static final Map<String,Integer> DISPLAY_MAP = new HashMap<>();
    static{
        DISPLAY_MAP.put("COUNT_BITS",COUNT_BITS);
        DISPLAY_MAP.put("CAPACITY",CAPACITY);
        DISPLAY_MAP.put("RUNNING",RUNNING);
        DISPLAY_MAP.put("SHUTDOWN",SHUTDOWN);
        DISPLAY_MAP.put("STOP",STOP);
        DISPLAY_MAP.put("TIDYING",TIDYING);
        DISPLAY_MAP.put("TERMINATED",TERMINATED);
    }



    public static void main(String[] args) {
        for (Map.Entry<String,Integer> entry : DISPLAY_MAP.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }



}
