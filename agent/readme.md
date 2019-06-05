### 个人理解
    将agent程序放入到main函数的项目中，提供Instrumentation对象调用JVMTI开放出来的功能。
    使得开发者可以对jvm进行一些灵活操作



### ManiFest文件
    Manifest-Version: 1.0
    Archiver-Version: Plexus Archiver
    Built-By: nero
    Created-By: Apache Maven 3.6.0
    Build-Jdk: 1.8.0_201
    Main-Class: Main
    Premain-Class: premain.Premain
    Agent-Class: agentmain.AgentMain



### 启动脚本：
    java -javaagent:./agent-1.0-SNAPSHOT.jar -jar agent-1.0-SNAPSHOT.jar

    -javaagent: 指明premain的代理程序，这个代理程序寻找的是agent，包含premain的jar
    -jar : 指明main方法入口，这个指明的main项目的jar
    
    
### AgentMain与PreMain的区别
    PreMain是在main函数执行之前运行的，具体的应用场景暂时不明 TODO
    PreMain是在main函数执行之后运行的，具体的应用场景暂时不明 TODO
        
 
### agentMain客户端
        /****
         *
         *
         * 将带有agentmain的jar attch到已经运行的进程中。前期是jar中含有agentmain类，并且manifest要指明
         * @param args
         * @throws IOException
         * @throws AgentLoadException
         * @throws AgentInitializationException
         * @throws AttachNotSupportedException
         */
        public static void main(String[] args) throws IOException, AgentLoadException, AgentInitializationException, AttachNotSupportedException {
            com.sun.tools.attach.VirtualMachine vm = VirtualMachine.attach("11385");
            vm.loadAgent("/Users/nero/code/githubmyself/BasicJava/agent/target/agent-1.0-SNAPSHOT.jar");
        }   
    
    
    






