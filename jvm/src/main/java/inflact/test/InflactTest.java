package inflact.test;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InflactTest {
    public static void targetMethod(){
        //打印堆栈信息
        new Exception().printStackTrace();
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> inflactTest =  Class.forName("inflact.test.InflactTest");
        Method method = inflactTest.getMethod("targetMethod");


        for (int i= 1 ; i < 20 ; i++){
            method.invoke(null,null);
        }


    }
}
