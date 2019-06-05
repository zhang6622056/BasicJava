package premain;

import java.lang.instrument.Instrumentation;

public class AgentMain {

    public static void agentmain(String args, Instrumentation instrumentation){
        System.out.println("this is the agentmain method...");


        Class[] classes = instrumentation.getAllLoadedClasses();
        for (Class cz : classes){
            System.out.println("-----------"+cz.getName()+"----------");
        }


    }



}
