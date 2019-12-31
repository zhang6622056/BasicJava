package objectmemory;


import objectmemory.bean.User;
import org.openjdk.jol.info.ClassLayout;

public class HashMemoryDemo {


    public static void main(String[] args) {

        final User a = new User();
        System.out.println("hash前："+ClassLayout.parseInstance(a).toPrintable());
        System.out.println("hashCode: " + Integer.toHexString(a.hashCode()));
        System.out.println("hash后："+ClassLayout.parseInstance(a).toPrintable());

        synchronized (a){
            System.out.println("for lock："+ClassLayout.parseInstance(a).toPrintable());
        }




    }








}
