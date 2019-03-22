package lambdas.basic.randoms;

import java.lang.reflect.Field;
import java.util.*;

/****
 * 用来生成满属性的JavaBean
 *
 *
 *
 */
public class RandomBean {

    private static final Random random = new Random();
    private static final Map<String,RandomAdapter> randomAdapter = new HashMap<>();



    private static void setFieldValue(Field f,Object obj) throws IllegalAccessException {
        f.setAccessible(true);
        //TODO-ZL 这里需要一个适配器
        f.set(obj,random.nextInt(10000));
    }


    /****
     * 生成一个beanList
     * @param clazz
     * @param count
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> List<T> generateBeanList(Class clazz,Long count) throws IllegalAccessException, InstantiationException {
       List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        List<T> list = new ArrayList<>();
        for (int i = 0 ; i < count ; i++){
            T obj = (T) clazz.newInstance();
            for (Field field : fields) setFieldValue(field,obj);
            list.add(obj);
        }
        return list;
    }






    interface RandomAdapter{

    }










}
