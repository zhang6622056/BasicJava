package premain;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Premain {



    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new Trans());
    }


    static class Trans implements ClassFileTransformer{
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            System.out.println(className);
            return null;
        }
    }




}
