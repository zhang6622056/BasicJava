package lambdas.basic;


import lambdas.basic.comflexfunction.Function;

import java.util.List;

/****
 * lambda 初接触
 */
public class LambdaBasic {

    //方法引用赋值，太特么的灵活了！
    private static final Print printUtil = System.out::println;

    /***
     * lambda表达式，用于 pa -> do something with pa
     */
    @FunctionalInterface
    interface MathOperation{
        Integer operate(Integer i);
    }


    /***
     * 打印的匿名函数
     */
    interface Print{
        void print(Object t);
    }

    /****
     * 执行减10操作。将方法作为函数引用赋值给MathOperator，引用ForMethodReference
     *
     * @param index
     * @return
     */
    public Integer ForMethodReference(Integer index){
        return index - 10;
    }


    /***
     * 同样是函数引用，但是为静态函数引用
     * @param index
     * @return
     */
    public static Integer ForMethodReferenceStatic(Integer index){
        return index - 20;
    }






    public Integer doMath(Integer i,MathOperation mathOperation) {
        return mathOperation.operate(i);
    }



    public static void main(String[] args) {
        LambdaBasic lambdaBasic = new LambdaBasic();

        //lambda表达式对于匿名函数的用法
        //System.out.println(lambdaBasic.doMath(1,minus -> minus+1));

        //-因参数和返回值与MathOperator相同，那么则可以建立方法引用，相当于调用ForMethodReference方法
//        MathOperation mathOperation = lambdaBasic::ForMethodReference;
//        System.out.println(mathOperation.operate(100));

        //-对于静态方法也可以使用
        MathOperation mathOperation = LambdaBasic::ForMethodReferenceStatic;
        System.out.println(mathOperation.operate(100));


        //-MethodReference引用为全局变量的情况
        //printUtil.print("kkk");
    }












}