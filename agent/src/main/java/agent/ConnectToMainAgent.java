package agent;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/****
 * the agent client
 */
public class ConnectToMainAgent {


    /****
     * 远程debug原理
     * @param args
     * @throws IOException
     * @throws AttachNotSupportedException
     * @throws AgentLoadException
     * @throws AgentInitializationException
     */
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        //已经启动的jvm pid
        VirtualMachine vm = VirtualMachine.attach("6399");
        vm.loadAgent("/Users/nero/code/githubmyself/BasicJava/agent/target/agent-1.0-SNAPSHOT.jar");
    }



}
