package agentmain;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.Location;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Map;


/****
 * agentMain代理
 * 对运行中的字节码进行变更
 *
 */
public class AgentMain {

    public static void agentmain(String args, Instrumentation instrumentation) throws InterruptedException, VMStartException, IllegalConnectorArgumentsException, IOException {
        instrumentation.addTransformer(new Trans());
        initEventListener();
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



    private static void initEventListener() throws VMStartException, IllegalConnectorArgumentsException, IOException, InterruptedException {
        LaunchingConnector launchingConnector
                = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> defaultArguments
                = launchingConnector.defaultArguments();
        VirtualMachine vm = launchingConnector.launch(defaultArguments);

        EventQueue eventQueue = vm.eventQueue();
        EventSet events = eventQueue.remove();
        EventIterator eventIterator = events.eventIterator();
        while(eventIterator.hasNext()){
            Event event = eventIterator.nextEvent();
            if (event instanceof ExceptionEvent){
                ExceptionEvent exceptionEvent = (ExceptionEvent) event;
                Location location = exceptionEvent.catchLocation();
                try{
                    System.out.println(location.sourceName());
                    System.out.println(location.lineNumber());
                }catch(Throwable t){
                    t.printStackTrace();
                }
            }
        }
    }
















}
