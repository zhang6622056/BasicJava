package agent;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

public class ConnectToMainAgent {


    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        VirtualMachine vm = VirtualMachine.attach("6399");
        vm.loadAgent("/Users/nero/code/githubmyself/BasicJava/agent/target/agent-1.0-SNAPSHOT.jar");
    }



}
