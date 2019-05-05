package overwriteAndOverride;


/*****
 * 在调用方法具有二义性的时候，调用的是哪一个方法呢?
 * 在jvm中匹配方法因素共有4个:  类名，方法名，传入类型参数，返回类型参数。
 * 1-在不允许自动拆装箱的基础下，不允许变长类型的情况下，检索匹配方法
 * 2-第一阶段没有匹配到的情况下，允许自动拆装箱，不允许变长类型，检索匹配方法
 * 3-第二阶段没有匹配到的情况下，允许自动拆装箱，允许变长类型，检索匹配方法。
 * java编译器在同一阶段找到多个匹配方法的时候，它会选择一个最贴近的方法。
 * 而贴近程度取决于形式参数类型的继承关系！！！越详细的类型，越是被匹配到。
 */
public class Overload {


    public Overload() {
    }

    /****
     * 变长参数
     * @param a
     * @param param
     * @param obj
     */
    void invoke(String a,Object param,Object... obj){
        System.out.println("second method....");
    }

    void invoke(String a,Object... obj){
        System.out.println("Integer method..");
    }


    /****
     *
     * @param args
     */
    public static void main(String[] args) {
        Overload overload = new Overload();
        overload.invoke(null,null,null);
    }
}
