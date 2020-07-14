package premain;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.isPublic;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class Premain {



    public static void premain(String args, Instrumentation instrumentation) {
       // instrumentation.addTransformer(new Trans());

        new AgentBuilder.Default()
                .type(isPublic()).transform(new AgentBuilder.Transformer() {
            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
                System.out.println("intercepted...");
                return builder.method(named("getUserName")).intercept(FixedValue.value("hello world!"));
            }
        }).installOn(instrumentation);
    }





}
