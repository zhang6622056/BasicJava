package inflact.test;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



/**
 *
 * 反射调用的第二个版本，在运行第十五次的时候将切换为动态实现
 * @author Nero
 * @date 2019-08-06
 * *@param: null
 * @return 
 */
public class InflactTestV2 {
    public static void targetMethod(int i){
        //打印堆栈信息
        new Exception("index : "+i).printStackTrace();
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> inflactTest =  Class.forName("inflact.test.InflactTestV2");
        Method method = inflactTest.getMethod("targetMethod",int.class);


        for (int i= 0 ; i < 300 ; i++){
            method.invoke(null,i);
        }
    }
}
