package objectmemory.outmemory;

import java.util.ArrayList;
import java.util.List;

public class OutMemoryBean {



    private static void testGc(){

        List<Integer> list = new ArrayList<>();
        for (int i = 200 ; i < 2200 ; i++){
            list.add(new Integer(i));
        }

    }



    public static void main(String[] args) throws InterruptedException {
        while(true){
            testGc();
        }


    }




}
