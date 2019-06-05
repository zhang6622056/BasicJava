package agentmain;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;


/****
 * agentMain代理
 * 对运行中的字节码进行变更
 *
 */
public class AgentMain {

    public static void agentmain(String args, Instrumentation instrumentation){
        instrumentation.addTransformer(new Trans());
    }


    /****
     *
     * 这里返回null 意味着不对字节码做任何更改
     */
    static class Trans implements ClassFileTransformer{
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            System.out.println(className);
            return null;
        }
    }






}
