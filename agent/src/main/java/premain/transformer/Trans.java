package premain.transformer;

import javassist.*;
import net.bytebuddy.ByteBuddy;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;




/***
 * @Description: 由于Agent程序并不会阻断code的执行，所以报错经常无法发现。建议程序体全部try catch
 * @Param: 
 * @return: 
 * @Author: zhanglei
 * @Date: 2020/7/10
 */
public class Trans implements ClassFileTransformer {
    final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
    final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";




    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!className.equals("agentmain/biz/UserService")){ return null; }

        //return generateByJavassist(className);

        return transformByByteBuddy(loader,className,classBeingRedefined,protectionDomain,classfileBuffer);
    }







    /**
     * @Description: 使用javassist生成字节码
     * @Param: [className]
     * @return: byte[]
     * @Author: zhanglei
     * @Date: 2020/7/13
     */
    private byte[] generateByJavassist(String className){
        byte[] res = null;
        try{
            //- replace的细节！！不能用 / 需要替换成.
            //- 将Classname 赋值给CtClass
            CtClass ctclass = ClassPool.getDefault().get(className.replaceAll("/", "."));
            for(CtMethod ctMethod : ctclass.getDeclaredMethods()){
                String methodName = ctMethod.getName();
                String newMethodName = methodName + "$old";// 新定义一个方法叫做比如sayHello$old
                ctMethod.setName(newMethodName);// 将原来的方法名字修改

                // 创建新的方法，复制原来的方法，名字为原来的名字
                CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctclass, null);

                // 构建新的方法体
                StringBuilder bodyStr = new StringBuilder();
                bodyStr.append("{");
                bodyStr.append("System.out.println(\"==============Enter Method: " + className + "." + methodName + " ==============\");");
                bodyStr.append(prefix);
                bodyStr.append(newMethodName + "($$);\n");// 调用原有代码，类似于method();($$)表示所有的参数
                bodyStr.append(postfix);
                bodyStr.append("System.out.println(\"==============Exit Method: " + className + "." + methodName + " Cost:\" +(endTime - startTime) +\"ms " + "===\");");
                bodyStr.append("}");

                newMethod.setBody(bodyStr.toString());// 替换新方法
                ctclass.addMethod(newMethod);// 增加新方法
            }
            res = ctclass.toBytecode();
        }catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }



    
    /***
     * @Description: 使用bytebuddy 生成字节码
     * @Param: [loader, className, classBeingRedefined, protectionDomain, classfileBuffer] 
     * @return: byte[]
     * @Author: zhanglei
     * @Date: 2020/7/13
     */
    private byte[] transformByByteBuddy(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer){
        ByteBuddy byteBuddy = new ByteBuddy();




        return null;
    }



}
